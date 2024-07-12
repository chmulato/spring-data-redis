package br.com.mulato.redis.repository;

import br.com.mulato.redis.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepository {

	public static final String HASH_KEY = "Product";

	@Autowired
	RedisTemplate<String, Product> template = new RedisTemplate<>();
	
	private List<Product> getList(List<Object> list) {
		List<Product> result = new ArrayList<Product>();
		for (Object object : list) {
			result.add((Product) object);
		}
		return result;
	}

	public Product save(Product product) {
		template.opsForHash().put(HASH_KEY, product.getId(), product);
		return product;
	}

	public List<Product> findAll() {
		List<Object> list = template.opsForHash().values(HASH_KEY);
		return getList(list);
	}

	public Product findProductById(int id) {
		return (Product) template.opsForHash().get(HASH_KEY, id);
	}

	public String deleteProduct(int id) {
		template.opsForHash().delete(HASH_KEY, id);
		return "product removed !!";
	}

}
