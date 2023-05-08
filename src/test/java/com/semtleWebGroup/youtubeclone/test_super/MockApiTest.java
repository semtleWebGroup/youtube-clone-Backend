package com.semtleWebGroup.youtubeclone.test_super;

import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

/**
 * It is almost identical to {@code @WebMvcTest}
 *
 * Conduct a test that is difficult to proceed with in the integrated test.
 * It is usually used when rollback is difficult (like an external payment API.)
 * or you need to use a Mock Service or Object.
 *
 *
 * Use {@code @MockBean} or {@code @SpyBean} annotation to create Mock.
 */
@WebMvcTest
@ActiveProfiles("test")
@Disabled
public class MockApiTest {

}