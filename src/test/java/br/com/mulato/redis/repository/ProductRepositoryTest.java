package br.com.mulato.redis.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import br.com.mulato.redis.config.RedisConfig;
import br.com.mulato.redis.entity.Product;

public class ProductRepositoryTest {

    @Mock
    private RedisTemplate<String, Product> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @InjectMocks
    private ProductRepository productDao;
	
	private List<Object> getList(List<Product> list) {
		List<Object> result = new ArrayList<Object>();
		for (Product product : list) {
			result.add((Object) product);
		}
		return result;
	}

    @BeforeEach
    public void setUp() {
    	RedisConfig config = new RedisConfig();
    	config.setHost("localhost");
    	config.setPort(6379);
    	redisTemplate = config.template();
        MockitoAnnotations.initMocks(this);
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    @Test
    public void testSave() {
        Product product = new Product();
        product.setId(1);
        productDao.save(product);
        verify(hashOperations, times(1)).put(ProductRepository.HASH_KEY, product.getId(), product);
    }

    @Test
    public void testFindAll() {
        Product product1 = new Product();
        product1.setId(1);
        Product product2 = new Product();
        product2.setId(2);
        List<Product> products = Arrays.asList(product1, product2);
        when(hashOperations.values(ProductRepository.HASH_KEY)).thenReturn(getList(products));
        List<Product> result = productDao.findAll();
        assertEquals(products, result);
    }

    @Test
    public void testFindProductById() {
        Product product = new Product();
        product.setId(1);
        when(hashOperations.get(ProductRepository.HASH_KEY, 1)).thenReturn(product);
        Product result = productDao.findProductById(1);
        assertEquals(product, result);
    }

    @Test
    public void testDeleteProduct() {
        productDao.deleteProduct(1);
        verify(hashOperations, times(1)).delete(ProductRepository.HASH_KEY, 1);
    }
}
