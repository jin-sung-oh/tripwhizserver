package com.example.demo.product.service;


import com.example.demo.product.domain.Product;
import com.example.demo.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;

@Service
public class ImageService {

    private static final Logger logger = LoggerFactory.getLogger(ImageService.class);

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public void saveImagesWithUrl(String directoryPath, Long pno) {
        logger.info("Starting to save images for product ID: {} from directory: {}", pno, directoryPath);

        Product product = productRepository.findById(pno)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + pno));

        File directory = new File(directoryPath);
        if (directory.exists() && directory.isDirectory()) {
            logger.info("Directory found: {}", directoryPath);

            File[] files = directory.listFiles();
            if (files != null && files.length > 0) {
                Arrays.stream(files)
                        .filter(File::isFile)
                        .forEach(file -> {
                            String fileName = file.getName();
                            String fileUrl = fileName;

                            // Product에 Image 추가
                            product.addImage(fileName, fileUrl);
                            logger.info("Added image to product - Filename: {}, URL: {}", fileName, fileUrl);
                        });

                // Product와 이미지를 함께 저장
                productRepository.save(product);
                logger.info("Product and images saved successfully.");
            } else {
                logger.warn("No files found in directory: {}", directoryPath);
            }
        } else {
            logger.error("Directory does not exist or is not a directory: {}", directoryPath);
        }
    }
}
