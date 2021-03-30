package muula.pocketpuppyschooljobs.security.filters;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class Filters {

  private final AuthFilter restFilter;

  @Bean
  public FilterRegistrationBean restRegistrationBean() {
    FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
    filterRegistrationBean.setFilter(restFilter);
    filterRegistrationBean.setUrlPatterns(Collections.singletonList("/ping"));
    return filterRegistrationBean;
  }
}
