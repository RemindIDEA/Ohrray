package com.ohrray.service;

import com.ohrray.Util.MapAPI;
import com.ohrray.Util.NaverSearchAPI;
import com.ohrray.domain.CommunityBoardDTO;
import com.ohrray.domain.MapDTO;
import com.ohrray.domain.PageRequestDTO;
import com.ohrray.entity.*;
import com.ohrray.repository.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.ServletException;
import lombok.RequiredArgsConstructor;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService{
    private final MemberRepository memberRepository;
    @Value("${communityImgPath}")
    private String uploadPath;

    @Value("${communityImgLocation}")
    private String ImgLocation;

    private final CommunityRepository communityRepository;
    private final MountainRepository mountainRepository;
    private final CommunityImgRepository imgRepository;
    private final CommunityBoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    //산 id로 커뮤니티 및 게시판 정보 불러오기.
    public Community getCommunityList(Long id){
        System.out.println("id = " + id);
        System.out.println("서비스 영역");
        return communityRepository.findByMountainId(id);
    }


    @Override
    public Page<CommunityBoardDTO> getCommunityBoardList(Long id, PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageble(Sort.by("id").descending());
        Page<CommunityBoardDTO> allCommunityBoard = boardRepository.findAllCommunityBoard(id,pageable).map(communityBoard -> {
            List<String> communityImg = new ArrayList<>();
            communityImg.add(communityBoard.getCommunityImg().getMainImg1());
            if(communityBoard.getCommunityImg().getMainImg2()!=null)
                communityImg.add(communityBoard.getCommunityImg().getMainImg2());
            if(communityBoard.getCommunityImg().getMainImg3()!=null)
                communityImg.add(communityBoard.getCommunityImg().getMainImg3());
            if(communityBoard.getCommunityImg().getMainImg4()!=null)
                communityImg.add(communityBoard.getCommunityImg().getMainImg4());
            CommunityBoardDTO communityBoardDTO =
                    new CommunityBoardDTO(communityBoard.getId(),communityBoard.getCommunity().getId(),communityImg
                            ,communityBoard.getTitle(),communityBoard.getContent());
            return communityBoardDTO;
        });
        return allCommunityBoard;
    }

    @Override
    public CommunityBoardDTO getOneBoard(Long bid) {
        CommunityBoard findOneBoard = boardRepository.findById(bid).orElseThrow(EntityNotFoundException::new);
        List<String> communityImg = new ArrayList<>();
        communityImg.add(findOneBoard.getCommunityImg().getMainImg1());
        if(findOneBoard.getCommunityImg().getMainImg2()!=null)
            communityImg.add(findOneBoard.getCommunityImg().getMainImg2());
        else {
            communityImg.add(null);
        }
        if(findOneBoard.getCommunityImg().getMainImg3()!=null)
            communityImg.add(findOneBoard.getCommunityImg().getMainImg3());
        else {
            communityImg.add(null);
        }
        if(findOneBoard.getCommunityImg().getMainImg4()!=null)
            communityImg.add(findOneBoard.getCommunityImg().getMainImg4());
        else {
            communityImg.add(null);
        }
        CommunityBoardDTO communityBoardDTO = CommunityBoardDTO.builder()
                .bid(findOneBoard.getId())
                .mid(findOneBoard.getMember().getId())
                .cid(findOneBoard.getCommunity().getId())
                .title(findOneBoard.getTitle())
                .content(findOneBoard.getContent())
                .fileName(communityImg)
                .regDate(findOneBoard.getRegDate())
                .modDate(findOneBoard.getModDate())
                .build();
        return communityBoardDTO;
    }

    //커뮤니티 게시글 등록
    @Transactional
    @Override
    public void registerBoard(CommunityBoardDTO boardDTO,String email) throws Exception {
        System.out.println(boardDTO.getImgFiles().toString());
        Member member = memberRepository.findByEmail(email).orElseThrow();
        MultipartFile[] mainImg = new MultipartFile[boardDTO.getImgFiles().size()];
        List<String> ImgFileName = new ArrayList<>();

        for (int i = 0; i < boardDTO.getImgFiles().size(); i++) {
            mainImg[i] = boardDTO.getImgFiles().get(i);
            ImgFileName.add(saveProductImg(mainImg[i]));
        }
        CommunityImg communityImg = CommunityImg.builder()
                .mainImg1(ImgFileName.get(0))
                .mainImg2(ImgFileName.size() >= 2 ? ImgFileName.get(1) : null)
                .mainImg3(ImgFileName.size() >= 3 ? ImgFileName.get(2) : null)
                .mainImg4(ImgFileName.size() >= 4 ? ImgFileName.get(3) : null)
                .build();
        imgRepository.save(communityImg);
        Community savedCommunity = communityRepository.findById(boardDTO.getCid()).orElseThrow(EntityNotFoundException::new);
        CommunityBoard board = CommunityBoard.builder()
                .community(savedCommunity)
                .member(member)
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .communityImg(communityImg)
                .build();
        boardRepository.save(board);
    }

    @Transactional
    @Override
    public void updateBoard(CommunityBoardDTO communityBoardDTO) throws Exception {
        //받아온 bid로 게시글 정보 가져오기
        CommunityBoard board = boardRepository.findById(communityBoardDTO.getBid()).orElseThrow(EntityNotFoundException::new);


        //이미지 변경할 엔티티 영속성에 올리기.
        CommunityImg updateCommunityImg = imgRepository.findById(board.getCommunityImg().getId()).orElseThrow(EntityNotFoundException::new);
        //수정전 상품 이미지
        CommunityImg excommunityImg = board.getCommunityImg();
        //수정된 이미지파일 객체


        ///메인이미지 파일 처리----------------------------------------------
        //파일형식의 메인이미지를 담아줄 배열생성
        List<MultipartFile> communityImgFiles = new ArrayList<>();
        //String 타입의 메인이미지 이름은 담아줄 List 생성
        List<String> communityFileNames = new ArrayList<>();

        int countOfUploadImg =  communityBoardDTO.getImgFiles().size();
        if(!communityBoardDTO.getImgFiles().get(0).getOriginalFilename().equals("")){
            //기존 이미지들 삭제
            deleteImg(excommunityImg);
            for (int i = 0; i < countOfUploadImg; i++) {
            communityImgFiles.add(communityBoardDTO.getImgFiles().get(i));
            System.out.println("mainImgFile = " + communityImgFiles.get(i).toString());
            communityFileNames.add(saveProductImg(communityImgFiles.get(i)));
            System.out.println("저장될 이미지이름"+i+ "= " + communityFileNames.get(i).toString());
            }
        }else{
            countOfUploadImg=0;
        }
        //계속 사용할 이미지 처리
        switch (countOfUploadImg){
            case 4:
                updateCommunityImg.setMainImg1(communityFileNames.get(0));
                updateCommunityImg.setMainImg2(communityFileNames.get(1));
                updateCommunityImg.setMainImg3(communityFileNames.get(2));
                updateCommunityImg.setMainImg4(communityFileNames.get(3));
                board.setCommunityImg(updateCommunityImg);
                break;
            case 3:

                updateCommunityImg.setMainImg1(communityFileNames.get(0));
                updateCommunityImg.setMainImg2(communityFileNames.get(1));
                updateCommunityImg.setMainImg3(communityFileNames.get(2));
                updateCommunityImg.setMainImg4(null);
                board.setCommunityImg(updateCommunityImg);
                break;
            case 2:

                updateCommunityImg.setMainImg1(communityFileNames.get(0));
                updateCommunityImg.setMainImg2(communityFileNames.get(1));
                updateCommunityImg.setMainImg3(null);
                updateCommunityImg.setMainImg4(null);
                board.setCommunityImg(updateCommunityImg);
                break;
            case 1:

                updateCommunityImg.setMainImg1(communityFileNames.get(0));
                updateCommunityImg.setMainImg2(null);
                updateCommunityImg.setMainImg3(null);
                updateCommunityImg.setMainImg4(null);
                board.setCommunityImg(updateCommunityImg);
                break;
            case 0:

                updateCommunityImg.setMainImg1(excommunityImg.getMainImg1());
                updateCommunityImg.setMainImg2(excommunityImg.getMainImg2());
                updateCommunityImg.setMainImg3(excommunityImg.getMainImg3());
                updateCommunityImg.setMainImg4(excommunityImg.getMainImg4());
                board.setCommunityImg(updateCommunityImg);
        }

        board.setTitle(communityBoardDTO.getTitle());
        board.setContent(communityBoardDTO.getContent());

    }

    @Override
    public Long deleteBoard(Long bid) {

        System.out.println("서비스 영역 = " + bid);
        CommunityBoard board = boardRepository.findById(bid).orElseThrow(EntityNotFoundException::new);
        boardRepository.deleteById(bid);
        deleteImg(board.getCommunityImg());
        imgRepository.deleteById(board.getCommunityImg().getId());

        return  board.getCommunity().getId();
    }





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
    public void deleteImg(CommunityImg excommunityImg){
        if (excommunityImg.getMainImg1() != null && !excommunityImg.getMainImg1().isEmpty()) {
            removeFile(excommunityImg.getMainImg1());
        }
        if (excommunityImg.getMainImg2() != null && !excommunityImg.getMainImg2().isEmpty()) {
            removeFile(excommunityImg.getMainImg2());
        }
        if (excommunityImg.getMainImg3() != null && !excommunityImg.getMainImg3().isEmpty()) {
            removeFile(excommunityImg.getMainImg3());
        }
        if (excommunityImg.getMainImg4() != null && !excommunityImg.getMainImg4().isEmpty()) {
            removeFile(excommunityImg.getMainImg4());
        }
    }


    //네이버 검색정보 Util 에서 가져오기.
    public String searchNaver(String keyword) throws ServletException, IOException {
        NaverSearchAPI naverSearchAPI = new NaverSearchAPI();
        return naverSearchAPI.searchNaver(keyword);
    }

    //초기에 API통해서 산 정보 집어넣기.
    public void insertMountainInform() throws IOException, ParseException {
        //초기에만 정보 없을 시 산 정보 DB에 입력해주기.
        List<MapDTO> list = new ArrayList<>();
        if(communityRepository.findAll().isEmpty()){
            MapAPI mapAPI = new MapAPI();
            list = mapAPI.getMapInformation();

            for(MapDTO mapDTO : list){
                Mountain mountain = Mountain.builder()
                        .name(mapDTO.getName())
                        .address(mapDTO.getAddress())
                        .height(mapDTO.getHeight())
                        .content(mapDTO.getContent())
                        .lat(mapDTO.getLat())
                        .lng(mapDTO.getLng())
                        .x(mapDTO.getX())
                        .y(mapDTO.getY()).build();

                mountainRepository.save(mountain);
                Community community = Community.builder()
                        .mountain(mountain)
                        .build();
                communityRepository.save(community);
            }
        }else{
            System.out.println("이미 정보가 있습니다.");
        }
    }

    //검색어 가지고 산 목록 보여주기
    @Override
    public Page<MapDTO> getMountainInformWithKeyword(String keyword, Integer pageNum) {
        Pageable pageable = PageRequest.of(pageNum,5, Sort.by("name").ascending());
        Page<MapDTO> result = mountainRepository.findByKeyword(keyword, pageable).map(mountain -> {
            MapDTO mapDTO =new MapDTO(mountain.getId(),mountain.getName(),mountain.getAddress(),
                    mountain.getHeight(),mountain.getContent(),mountain.getLat(),mountain.getLng(),
                    mountain.getX(),mountain.getY());
            return mapDTO;
        });

        if (result.isEmpty())
            return null;

        return result;
    }
}

