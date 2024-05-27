package com.ohrray;

import com.ohrray.entity.Member;
import com.ohrray.entity.ProductCategory;
import com.ohrray.repository.LoginRepository;
import com.ohrray.repository.ProductCategoryRepository;
import com.ohrray.service.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
class OhrrayApplicationTests {
	@Autowired
	LoginRepository loginRepository;

	@Autowired
	ProductCategoryRepository categoryRepository;

	@Value("${uploadPath}")
	private String uploadPath;

	@Autowired
	ProductServiceImpl productService;

	@Test
	void insertMember() {

		IntStream.rangeClosed(11, 20).forEach(i -> {
			Member member = Member.builder()
					.email("a" + i + "@a" + i + "com")
					.password("1234")
					.build();
			loginRepository.save(member);
		});


	}

	@Test
	void insertCate() {
		ProductCategory cate = ProductCategory.builder()
				.mainCategory("clothes")
				.subCategory("top")
				.build();
		categoryRepository.save(cate);
		ProductCategory cate1 = ProductCategory.builder()
				.mainCategory("clothes")
				.subCategory("bottom")
				.build();
		categoryRepository.save(cate1);
		ProductCategory cate2 = ProductCategory.builder()
				.mainCategory("clothes")
				.subCategory("outter")
				.build();
		categoryRepository.save(cate2);
		ProductCategory cate3 = ProductCategory.builder()
				.mainCategory("clothes")
				.subCategory("down")
				.build();
		categoryRepository.save(cate3);
		ProductCategory cate4= ProductCategory.builder()
				.mainCategory("shoes")
				.subCategory("hikingShoes")
				.build();
		categoryRepository.save(cate4);
		ProductCategory cate5 = ProductCategory.builder()
				.mainCategory("shoes")
				.subCategory("trekkingShoes")
				.build();
		categoryRepository.save(cate5);
		ProductCategory cate6 = ProductCategory.builder()
				.mainCategory("shoes")
				.subCategory("aijen")
				.build();
		categoryRepository.save(cate6);
		ProductCategory cate7 = ProductCategory.builder()
				.mainCategory("shoes")
				.subCategory("lifeStyle")
				.build();
		categoryRepository.save(cate7);
		ProductCategory cate8 = ProductCategory.builder()
				.mainCategory("bag")
				.subCategory("backPack")
				.build();
		categoryRepository.save(cate8);
		ProductCategory cate9 = ProductCategory.builder()
				.mainCategory("bag")
				.subCategory("accBag")
				.build();
		categoryRepository.save(cate9);
		ProductCategory cate10 = ProductCategory.builder()
				.mainCategory("equit")
				.subCategory("cap")
				.build();
		categoryRepository.save(cate10);
		ProductCategory cate11 = ProductCategory.builder()
				.mainCategory("equit")
				.subCategory("stick")
				.build();
		categoryRepository.save(cate11);
		ProductCategory cate12 = ProductCategory.builder()
				.mainCategory("equit")
				.subCategory("gloves")
				.build();
		categoryRepository.save(cate12);
		ProductCategory cate13 = ProductCategory.builder()
				.mainCategory("equit")
				.subCategory("socks")
				.build();
		categoryRepository.save(cate13);

	}

	@Test
	public void  removefile (){
		String fileName="221a2326-7a3f-4843-ae75-f0c039ecf894";
		productService.removeFile(fileName);
	}


}
