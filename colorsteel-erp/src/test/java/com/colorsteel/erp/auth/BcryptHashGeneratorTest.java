package com.colorsteel.erp.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 需要重新生成演示密码哈希时，去掉 {@link Disabled} 后运行一次测试即可。
 */
class BcryptHashGeneratorTest {

    @Test
    @Disabled("仅本地需要新哈希时启用")
    void printBcryptForAdmin123() {
        BCryptPasswordEncoder enc = new BCryptPasswordEncoder();
        String hash = enc.encode("admin123");
        Assertions.assertTrue(enc.matches("admin123", hash));
        System.out.println("BCrypt(admin123) = " + hash);
    }
}
