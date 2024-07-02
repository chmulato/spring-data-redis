package br.com.mulato.redis.repository;

import br.com.mulato.redis.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDao {

	public static final String HASH_KEY = "Product";

	@SuppressWarnings("rawtypes")
	@Autowired
	private RedisTemplate template;

	@SuppressWarnings("unchecked")
	public Product save(Product product) {
		template.opsForHash().put(HASH_KEY, product.getId(), product);
		return product;
	}

	@SuppressWarnings("unchecked")
	public List<Product> findAll() {
		return template.opsForHash().values(HASH_KEY);
	}

	@SuppressWarnings("unchecked")
	public Product findProductById(int id) {
		return (Product) template.opsForHash().get(HASH_KEY, id);
	}

	@SuppressWarnings("unchecked")
	public String deleteProduct(int id) {
		template.opsForHash().delete(HASH_KEY, id);
		return "product removed !!";
	}

}
