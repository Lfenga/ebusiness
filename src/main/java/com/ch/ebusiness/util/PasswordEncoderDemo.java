package com.ch.ebusiness.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Scanner;

public class PasswordEncoderDemo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入明文密码：");
        String rawPassword = scanner.nextLine();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("数据库存储密码（BCrypt加密）：");
        System.out.println(encodedPassword);

        //78f8a7ae700c91db09facb05a675432b
        //$2a$10$8rvAxQRBEnFEJJRIzApp1uk5rsEEcP1ZkhiKmECRF3pqe6zEcyjGi
    }
}