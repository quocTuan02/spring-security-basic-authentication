package com.tuannq.authentication;

import com.tuannq.authentication.repository.*;
import com.tuannq.authentication.service.MailService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
class ApplicationTests {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private BrandRepository brandRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private SpecificationRepository specificationRepository;
    @Autowired
    private ProductSpecificationRepository productSpecificationRepository;

    @Test
    void test() {
        String test = "write test case here!";
        Assertions.assertNotNull(test);
    }

    @Test
    void test1() {
        var product = productRepository.findById(1L);
        if (product.isEmpty()) return;
        var specification = specificationRepository.findById(6L);
        if (specification.isEmpty()) return;
        var productSpecifications = new ProductSpecifications(specification.get(), product.get(), "2021 ");

        ProductSpecifications save = productSpecificationRepository.save(productSpecifications);
        int a = 1;

    }

}
