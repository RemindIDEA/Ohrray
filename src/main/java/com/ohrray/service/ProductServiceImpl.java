package com.ohrray.service;

import com.ohrray.domain.*;
import com.ohrray.entity.*;
import com.ohrray.enums.DetailStatus;
import com.ohrray.enums.PayStatus;
import com.ohrray.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2

public class ProductServiceImpl implements ProductService{

    private final MemberRepository memberRepository;
    @Value("${uploadPath}")
    private String uploadPath;

    @Value("${productImgLocation}")
    private String ImgLocation;

    private final ProductRepository productRepository;

    private final ProductImgRepository productImgRepository;

    private  final ProductCategoryRepository categoryRepository;

    private final  OptionRepository optionRepository;

    private final OrdersRepository ordersRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final CartRepository cartRepository;

/*상품등록 service*************************************************************************/
    @Transactional
    @Override
    public void registerProductSales(ProductFormDTO productFormDTO ,String email) throws Exception {

        Member member = memberRepository.findByEmail(email).orElseThrow();

        System.out.println("register서비스시작");
        /*formDTO에서 각 DTO에 분배 ************************************************************/
        ProductImgDTO productImgDTO = new ProductImgDTO();
        productImgDTO.setData(productFormDTO.getMainImg(),productFormDTO.getDetailImg());

        ProductDTO productDTO = new ProductDTO();
        productDTO.setData(productFormDTO.getProductName(), productFormDTO.getProductPrice(), productFormDTO.getReadCount(),productImgDTO);

        ProductCategotyDTO cateDTO = new ProductCategotyDTO();
        cateDTO.setData(productFormDTO.getMainCategory(), productFormDTO.getSubCategory());

        /*formDTO에서 각 DTO에 분배 ************************************************************/


        /*이미지 파일 등록  및 업로드**********************************************************************************/
        //메인이미지 파일 처리----------------------------------------------
        //파일형식의 메인이미지를 담아줄 배열생성
        MultipartFile[] mainImgFile = new MultipartFile[3];
        //String 타입의 메인이미지 이름은 담아줄 List 생성
        List<String> mainInformation = new ArrayList<>();
        for (int i = 0; i < 3 && i < productImgDTO.getMainImg().size(); i++) {
            mainImgFile[i] = productFormDTO.getMainImg().get(i);
            mainInformation.add(saveProductImg(mainImgFile[i]));
        }
        //상세이미지 파일 처리-------------------------------------------------------------
        // 파일 형식의 상세 이미지를 담아줄 배열 생성
        MultipartFile[] detailImgFile =new MultipartFile[3];
        // String 타입의 상세 이미지 이름을 담아줄 List 생성
        List<String> detailInformation = new ArrayList<>();
        for(int i = 0; i<3 && i<productImgDTO.getDetailImg().size(); i++){
            detailImgFile[i] =productFormDTO.getDetailImg().get(i);
            detailInformation.add(saveProductImg(detailImgFile[i]));
        }

        //productImgDTO -> ProductImg(Entity)변환
        // ProductImg 빌더를 사용하여 객체 생성
        ProductImg savedProductImg = ProductImg.builder()
                .mainImg1(productFormDTO.getMainImg().size() >= 1 ? mainInformation.get(0) : null)
                .mainImg2(productFormDTO.getMainImg().size() >= 2 ? mainInformation.get(1) : null)
                .mainImg3(productFormDTO.getMainImg().size() >= 3 ? mainInformation.get(2) : null)
                .detailImg1(productFormDTO.getDetailImg().size() >= 1 ? detailInformation.get(0) : null)
                .detailImg2(productFormDTO.getDetailImg().size() >= 2 ? detailInformation.get(1) : null)
                .detailImg3(productFormDTO.getDetailImg().size() >= 3 ? detailInformation.get(2) : null)
                .build();
                productImgRepository.save(savedProductImg);
        System.out.println("저장된 이미지 파일들 = " + savedProductImg.toString());
        /*이미지 파일 등록 및 업로드  끝 ************************************************************************/

        /*카테고리의 등록 찾아오기 **************************************************************/
        System.out.println("cateDTO.getSubCategory() = " + cateDTO.getSubCategory());
        ProductCategory category = ProductCategory.builder()
                .mainCategory(cateDTO.getMainCategory())
                .subCategory(cateDTO.getSubCategory())
                .build();
        ProductCategory cateSample = categoryRepository.save(category);
        System.out.println("등록된 상품의 카테고리 번호는 :  " + cateSample.getId());
        /*카테고리의 등록  끝**********************************************************/


        // formDTO 에서 OPtionDTO 로 분리
        List<Option> optionList = new ArrayList<>();
        //option에서 color size stock 분리

        for (ProductOptionDTO str : productFormDTO.getOption()){
            ProductOptionDTO optionDTO =new ProductOptionDTO();
            String fullOption=str.getColor() + "   -   " + str.getSize();
            optionDTO.setData(str.getColor(),str.getSize(), str.getStock(), fullOption);
            System.out.println("optionDTO의색상은? = " + optionDTO.getColor());
            Option option = Option.builder()
                    .color(optionDTO.getColor())
                    .size(optionDTO.getSize())
                    .stock(optionDTO.getStock())
                    .fullOption(optionDTO.getFullOption())
                    .build();
            optionList.add(option);
        }


        /*상품등록 ***********************************************************************************/
        Product product = Product
                .builder()
                .id(productDTO.getPno())
                .member(member)
                .productName(productDTO.getProductName())
                .productPrice(productDTO.getProductPrice())
                .productImg(savedProductImg)
                .productCategory(category)
                .options(optionList)
                .build();
        System.out.println("product.getProductName() = " + product.getProductName());
        productRepository.save(product);
        /*사이즈 및 옵션값 처리 ***************************************************/
        System.out.println("옵션들~ = " + optionList.get(0).toString());
        for(Option option: optionList){
            option.setProduct(product);
            optionRepository.save(option);
        }
        /*사이즈 및 옵션값 처리 **************************************************/
    }

    /*상품등록 끝 **************************************************************************/

    //이미지 파일 에서 파일명 추출(UUID적용된)
    public  String saveProductImg(MultipartFile productImg) throws Exception {
        String oriImgName=productImg.getOriginalFilename();
        System.out.println("oriImgName = " + oriImgName);
        String imgName= "";
        String imgUrl="";
        //파일업로드
        if(!StringUtils.isEmpty(oriImgName)){
            imgName =uploadFile(ImgLocation,oriImgName,productImg.getBytes());

        }
        return imgName;

    }


    //파일업로드 + 파일명 UUID로 변경
    public String uploadFile(String uploadPath,String originalFileName,byte[] fileData) throws IOException {
        //UUID 생성
        UUID uuid = UUID.randomUUID();
        //확장자 구하기
        String extension  =originalFileName.substring(originalFileName.lastIndexOf("."));
        String savedFileName = uuid.toString() + extension;
        String fileUploadFullUrl= uploadPath+"/"+savedFileName;
        System.out.println("savedFileName = " + savedFileName);
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();
        return savedFileName;
    }
/* 상품 등록  service끝 *************************************************************************************************************/

/* 모든상품 조회 service **********************************************************************************************************/

    @Override
    public PageResultDTO<ProductFormDTO,Object[]> findSalesProduct(PageRequestDTO requestDTO) {

        System.out.println("서비스 시작");
        Pageable pageable = requestDTO.getPageble(Sort.by("id").descending());
        System.out.println("requestDTO.키워드 = " + requestDTO.getKeyword());
        System.out.println("requestDTO.페이지사이즈 = " + requestDTO.getSize());
        Page<Object[]> listPage = productRepository.getListPage(pageable,requestDTO.getKeyword());



        System.out.println( "===============================");;
        listPage.getContent().forEach(arr -> {
            System.out.println("arr.toString() = " + arr.toString());
        });
        
        
        List<ProductFormDTO> collect = listPage.stream().map(product ->
                        ProductFormDTO
                                .builder()
                                .pno(((Product)product[0]).getId())
                                .productName(((Product) product[0]).getProductName())
                                .productPrice(((Product)product[0]).getProductPrice())
                                .cno(((Product)product[0]).getProductCategory().getId())
                                .mainCategory(((Product)product[0]).getProductCategory().getMainCategory())
                                .subCategory(((Product)product[0]).getProductCategory().getSubCategory())
                                .mainImgName(Arrays.asList(
                                        ((Product) product[0]).getProductImg().getMainImg1(),
                                        ((Product) product[0]).getProductImg().getMainImg2(),
                                        ((Product) product[0]).getProductImg().getMainImg3()
                                ))
                                .build()).collect(Collectors.toList());
        
        PageResultDTO<ProductFormDTO, Object[]> productFormDTOPageResultDTO = new PageResultDTO<>(listPage, collect);
        System.out.println("productFormDTOPageResultDTO.getDtoList() = " + productFormDTOPageResultDTO.getDtoList());
        return productFormDTOPageResultDTO;


    }
    /* 모든상품 조회 service  끝**********************************************************************************************************/

    /* 상품 상세내역 조회 service  **********************************************************************************************************/

    @Override
    public ProductFormDTO findOneProduct(Long pno) {
        System.out.println("서비스도착");
        Product oneProduct = productRepository.getOneProduct(pno);
        List<Option> optionList = optionRepository.findbyProduct_id(pno);
        List<ProductOptionDTO> optionDTO = optionList.stream().map(
                option -> ProductOptionDTO.builder()
                        .ono(option.getId())
                        .size(option.getSize())
                        .color(option.getColor())
                        .stock(option.getStock())
                        .fullOption(option.getFullOption())
                        .build()).toList();

        System.out.println("oneProduct.toString() = " + oneProduct.toString());
        ProductFormDTO dto = ProductFormDTO.builder()
                .pno(oneProduct.getId())
                .mid(oneProduct.getMember().getId())
                .productName(oneProduct.getProductName())
                .productPrice(oneProduct.getProductPrice())
                .readCount(oneProduct.getReadCount())
                .mainImgName(Arrays.asList(
                        oneProduct.getProductImg().getMainImg1(),
                        oneProduct.getProductImg().getMainImg2(),
                        oneProduct.getProductImg().getMainImg3()))
                .detailImgName(Arrays.asList(
                        oneProduct.getProductImg().getDetailImg1(),
                        oneProduct.getProductImg().getDetailImg2(),
                        oneProduct.getProductImg().getDetailImg3()))
                .cno(oneProduct.getProductCategory().getId())
                .mainCategory(oneProduct.getProductCategory().getMainCategory())
                .subCategory(oneProduct.getProductCategory().getSubCategory())
                .option(optionDTO)
                .build();
        System.out.println("dto.toString() = " + dto.toString());

        return dto;

    }
    /* 상품 상세내역 조회 service  끝**********************************************************************************************************/




    /*상품 수정 service ****************************************************************************/
    @Transactional
    @Override
    public ProductFormDTO updateProduct(ProductFormDTO productFormDTO) throws Exception {
        System.out.println("수정 서비스 시작");
        //수정전 상품
        Product product = productRepository.findById(productFormDTO.getPno()).orElseThrow();
        //카테고리 수정
        ProductCategotyDTO cateDTO = new ProductCategotyDTO();
        cateDTO.setData(productFormDTO.getMainCategory(), productFormDTO.getSubCategory());

        ProductCategory category = categoryRepository.findById(product.getProductCategory().getId()).orElseThrow();
        category.setMainCategory(cateDTO.getMainCategory());
        category.setSubCategory(cateDTO.getSubCategory());
        //기존 상품 옵션값 삭제
        List<Option> options = optionRepository.findbyProduct_id(product.getId());
        for (Option option : options){
            optionRepository.delete(option);
        }
        //이미지 변경할 엔티티 영속성에 올리기
        ProductImg updateProductImg = productImgRepository.findById(product.getProductImg().getId()).orElseThrow();

        //수정전 상품 이미지
        ProductImg EXproductImg = product.getProductImg();

        //수정된 이미지파일 객체
        ProductImgDTO productImgDTO = new ProductImgDTO();
        productImgDTO.setData(productFormDTO.getMainImg(),productFormDTO.getDetailImg());

        ///메인이미지 파일 처리----------------------------------------------
        //파일형식의 메인이미지를 담아줄 배열생성
        List<MultipartFile> mainImgFile = new ArrayList<>();
        //String 타입의 메인이미지 이름은 담아줄 List 생성
        List<String> mainInformation = new ArrayList<>();

        int countOfUploadMainImg =  productImgDTO.getMainImg().size();
        System.out.println("===================="+productFormDTO.getMainImg().get(0).getOriginalFilename().equals(""));
        if(!productFormDTO.getMainImg().get(0).getOriginalFilename().equals("")){
            for (int i = 0; i < countOfUploadMainImg; i++) {
                mainImgFile.add(productFormDTO.getMainImg().get(i));
                System.out.println("mainImgFile = " + mainImgFile.get(i).toString());
                mainInformation.add(saveProductImg(mainImgFile.get(i)));
                System.out.println("저장될 이미지이름"+i+ "= " + mainInformation.get(i).toString());
            }
        }else{
            countOfUploadMainImg=0;
        }


        //기존에  메인 이미지 삭제 처리
        if(countOfUploadMainImg!=0){
            if (EXproductImg.getMainImg3() != null && !EXproductImg.getMainImg3().isEmpty()) {
                removeFile(EXproductImg.getMainImg3());
            }
            if (EXproductImg.getMainImg2() != null && !EXproductImg.getMainImg2().isEmpty()) {
                removeFile(EXproductImg.getMainImg2());
            }
            if (EXproductImg.getMainImg1() != null && !EXproductImg.getMainImg1().isEmpty()) {
                removeFile(EXproductImg.getMainImg1());
            }
        }

        //상세이미지 파일 처리-------------------------------------------------------------
        // 파일 형식의 상세 이미지를 담아줄 배열 생성
        List<MultipartFile> detailImgFile =new ArrayList<>();
        // String 타입의 상세 이미지 이름을 담아줄 List 생성
        List<String> detailInformation = new ArrayList<>();
        int countOfUploadDetailImg =  productImgDTO.getDetailImg().size();
        System.out.println("countOfUploadDetailImg = " + countOfUploadDetailImg);

        System.out.println("===================="+productFormDTO.getMainImg().get(0).getOriginalFilename().equals(""));
        if(!productFormDTO.getDetailImg().get(0).getOriginalFilename().equals("")){
            for(int i = 0; i<countOfUploadDetailImg; i++){
                detailImgFile.add(productFormDTO.getDetailImg().get(i));
                detailInformation.add(saveProductImg(detailImgFile.get(i)));
                System.out.println("저장될 상세이미지 파일 이름은? = " + detailInformation.get(i));
            }
        }else {
            countOfUploadDetailImg=0;
        }

        //기존 상세 이미지 파일 제거
        if(countOfUploadDetailImg!=0){
            if (EXproductImg.getDetailImg3() != null && !EXproductImg.getDetailImg3().isEmpty()) {
                removeFile(EXproductImg.getDetailImg3());
            }
            if (EXproductImg.getDetailImg2() != null && !EXproductImg.getDetailImg2().isEmpty()) {
                removeFile(EXproductImg.getDetailImg2());
            }
            if (EXproductImg.getDetailImg1() != null && !EXproductImg.getDetailImg1().isEmpty()) {
                removeFile(EXproductImg.getDetailImg1());
            }
        }

        //productImgDTO -> ProductImg(Entity)변환
        // ProductImg 빌더를 사용하여 객체 생성
        updateProductImg.setMainImg1(mainInformation.size() >= 1 ? mainInformation.get(0) : null);
        updateProductImg.setMainImg2(mainInformation.size() >= 2 ? mainInformation.get(1) : null);
        updateProductImg.setMainImg3(mainInformation.size() >= 3 ? mainInformation.get(2) : null);
        updateProductImg.setDetailImg1(detailInformation.size() >= 1 ? detailInformation.get(0) : null);
        updateProductImg.setDetailImg2(detailInformation.size() >= 2 ? detailInformation.get(1) : null);
        updateProductImg.setDetailImg3(detailInformation.size() >= 3 ? detailInformation.get(2) : null);
        productImgRepository.save(updateProductImg);
        System.out.println("수정된 이미지 파일중 메인1번 = " + updateProductImg.getMainImg1());



        /*옵션값 수정 ------------------------------------*/
        List<Option> optionList = new ArrayList<>();
        System.out.println("신규 옵션DTO 등록");
        for (ProductOptionDTO str : productFormDTO.getOption()){
            if (str.getColor().isEmpty()){
                continue;
            }
            String fullOption =str.getColor() +"   -   "+ str.getSize();
            ProductOptionDTO optionDTO =new ProductOptionDTO();
            optionDTO.setData(str.getColor(),str.getSize(), str.getStock(), fullOption);
            System.out.println("optionDTO = " + optionDTO.getColor());
            Option option = Option.builder()
                    .color(optionDTO.getColor())
                    .size(optionDTO.getSize())
                    .stock(optionDTO.getStock())
                    .fullOption(optionDTO.getFullOption())
                    .product(product)
                    .build();
            optionRepository.save(option);
            optionList.add(option);
        }
        /*옵션값 수정 ------------------------------------*/



        //기존 Product 호출

        product.setProductName(productFormDTO.getProductName());
        product.setProductPrice(productFormDTO.getProductPrice());
        product.setReadCount(productFormDTO.getReadCount());
        product.setProductCategory(category);
        product.setProductImg(updateProductImg);
        product.setOptions(optionList);

        return productFormDTO;

    }
    /*상품 수정 service  끝****************************************************************************/

    @Transactional
    @Override
    public void insertOrder(PaymentDTO paymentDTO,AddPaymentDTO addPaymentDTO) {

        //회원 정보 가져오기
        Cart cart = cartRepository.findById(addPaymentDTO.getCartId().get(0)).orElseThrow();
        Member member = memberRepository.findByEmail(cart.getMember().getEmail()).orElseThrow();

        List<Long> cartList = addPaymentDTO.getCartId();
        //오더 먼저 등록
        Orders orders = Orders.builder()
                .member(member)
                .orderAddress(paymentDTO.getOrderAddress())
                .recipient(paymentDTO.getRecipient())
                .totalPrice(paymentDTO.getTotalPrice())
                .receiptId(paymentDTO.getReceiptId())
                .paymentStatus(paymentDTO.getPaymentStatus().equals("결제완료")? PayStatus.COMPLETE : PayStatus.CANCLE)
                .build();
        Orders saveOrder = ordersRepository.save(orders);

        for(Long cartId : cartList){
            Cart getCart = cartRepository.findById(cartId).orElseThrow();
            OrderDetail orderDetail = OrderDetail.builder()
                    .orders(saveOrder)
                    .product(getCart.getProduct())
                    .productPriceNow(getCart.getProduct().getProductPrice())
                    .count(getCart.getProductCount())
                    .detailStatus(DetailStatus.PROCESS)
                    .build();
            OrderDetail savedOrderDetail = orderDetailRepository.save(orderDetail);
            //저장된 OrderDetail 내용 연결시켜주기
            System.out.println("saveOrder.getOrderDetailList() = " + saveOrder.getOrderDetailList());
            System.out.println("savedOrderDetail.getProductPriceNow() = " + savedOrderDetail.getProductPriceNow());
            saveOrder.plusOrderDetail(savedOrderDetail);
            //전부 입력 한 후에 카트 내용 삭제시켜서 카트 비우기
            cartRepository.deleteById(cartId);
        }
    }

    /*상품 삭제 service *****************************************************************************************/
    @Transactional
    @Override
    public void deleteProduct(Long pno) {
        //상품 및 상품이미지 조회

        Optional<Product> EXProduct = productRepository.findById(pno);
        System.out.println("EXProduct.get().toString() = " + EXProduct.get().toString());
        //기존 업로드 된 이미지파일  삭제
        ProductImg EXproductImg = EXProduct.get().getProductImg();
        if (EXproductImg != null) {
            if (EXproductImg.getMainImg1() != null && !EXproductImg.getMainImg1().isEmpty()) {
                removeFile(EXproductImg.getMainImg1());
            }
            if (EXproductImg.getMainImg2() != null && !EXproductImg.getMainImg2().isEmpty()) {
                removeFile(EXproductImg.getMainImg2());
            }
            if (EXproductImg.getMainImg3() != null && !EXproductImg.getMainImg3().isEmpty()) {
                removeFile(EXproductImg.getMainImg3());
            }
            if (EXproductImg.getDetailImg1() != null && !EXproductImg.getDetailImg1().isEmpty()) {
                removeFile(EXproductImg.getDetailImg1());
            }
            if (EXproductImg.getDetailImg2() != null && !EXproductImg.getDetailImg2().isEmpty()) {
                removeFile(EXproductImg.getDetailImg2());
            }
            if (EXproductImg.getDetailImg3() != null && !EXproductImg.getDetailImg3().isEmpty()) {
                removeFile(EXproductImg.getDetailImg3());
            }
        }
        //이미지 데이터 삭제
        productImgRepository.deleteById(EXProduct.get().getProductImg().getId());

        //등록된 옵션삭제
        List<Option> optionList = optionRepository.findbyProduct_id(pno);
        for (Option option : optionList){
            optionRepository.delete(option);
        }

        //연결된 카테고리 삭제
        categoryRepository.deleteById(EXProduct.get().getProductCategory().getId());

        //상품삭제
        productRepository.delete(EXProduct.get());


    }
    /*상품 삭제 service  끝*****************************************************************************************/
    /*등록된 이미지 파일 삭제 *********************************************************************/
    public ResponseEntity<Boolean> removeFile(String fileName) {
        String srcFileName = null;
        try {
            srcFileName = URLDecoder.decode(fileName, "UTF-8");
            File file = new File(uploadPath + File.separator + srcFileName);

            if (!file.exists()) {
                // 파일이 존재하지 않는 경우에 대한 처리
                throw new FileNotFoundException("File not found: " + srcFileName);
            }

            boolean result = file.delete(); // 원본 삭제
            if (!result) {
                // 파일 삭제에 실패한 경우에 대한 처리
                throw new IOException("Failed to delete file: " + srcFileName);
            }

            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            // URL 디코딩 중 발생한 예외 처리
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (FileNotFoundException e) {
            // 파일이 존재하지 않는 경우에 대한 예외 처리
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            // 파일 삭제 실패 등의 I/O 예외 처리
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   /*등록된 이미지 파일 삭제 *********************************************************************/

}
