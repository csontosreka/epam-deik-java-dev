package com.epam.training.webshop.ui.configuration;

import com.epam.training.webshop.core.cart.grossprice.GrossPriceCalculator;
import com.epam.training.webshop.core.cart.grossprice.impl.GrossPriceCalculatorImpl;
import com.epam.training.webshop.core.cart.grossprice.impl.HungarianTaxGrossPriceCalculator;
import com.epam.training.webshop.core.product.ProductService;
import com.epam.training.webshop.core.product.ProductServiceImpl;
import com.epam.training.webshop.ui.command.impl.AbstractCommand;
import com.epam.training.webshop.ui.command.impl.UserAddProductToCartCommand;
import com.epam.training.webshop.ui.command.impl.UserCheckoutCartCommand;
import com.epam.training.webshop.ui.command.impl.UserProductListCommand;
import com.epam.training.webshop.ui.interpreter.CommandLineInterpreter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
@ComponentScan("com.epam.training.webshop")
public class ApplicationConfiguration {

    @Bean
    public ProductService productService() {
        return new ProductServiceImpl();
    }

    @Bean
    public GrossPriceCalculator grossPriceCalculator() {
        return new HungarianTaxGrossPriceCalculator(new GrossPriceCalculatorImpl());
    }

    @Bean
    public AbstractCommand userProductListCommand(ProductService productService) {
        return new UserProductListCommand(productService);
    }

    @Bean
    public AbstractCommand userAddProductToCartCommand(ProductService productService) {
        return new UserAddProductToCartCommand(productService);
    }

    @Bean
    public AbstractCommand userCheckoutCartCommand(GrossPriceCalculator grossPriceCalculator) {
        return new UserCheckoutCartCommand(grossPriceCalculator);
    }

    @Bean
    public CommandLineInterpreter commandLineInterpreter(Set<AbstractCommand> commands) {
        return new CommandLineInterpreter(System.in, System.out, commands);
    }

}