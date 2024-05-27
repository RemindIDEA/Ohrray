document.addEventListener('DOMContentLoaded',function (){



    //댓글 가져오기
    function getListReview() {
        var pno = document.querySelector(".pno").value;
        console.log(pno);
        $.ajax({
            url: "/reply/shopping/getList", // 요청 URL
            type: "get", // HTTP 메서드(전송방식)
            data: { // 매개변수로 전달할 데이터
                pno: pno // 부모글번호
            },
            dataType: "json", // 응답 데이터 형식
            success: sucFuncJson1, // 요청 성공 시 호출할 메서드 설정
            error: errFunc1 // 요청 실패 시 호출할 메서드 설정
        });
    }

    function sucFuncJson1(result) {
        if (result){
            var str = "";
            result.forEach(function (list, index) {
                str += "<li class='left clearfix' data-rno='" + list.id + "'>";
                str += "<input type='text' class='updateContent' placeholder='변경할 내용 입력' style='display: none'>";
                str += "  <div><div class='header'><strong class='primary-font'>["
                    + list.id + "] " + list.mid + "</strong>";
                str += "    <small class='pull-right text-muted'>"
                    + list.regDate + "</small></div>";
                str += "    <p>" + list.review + "</p></div>"
                str += "    <p>" + list.rating + "</p>"
                str += "--->   <p>" + list.answer + "</p>"
                str += "<input type='hidden' class='reviewId' th:value='" + list.id + "'/>"
                str += " <button class='deleteReview float-right' onclick='deleteReview(event)' name='"+list.id+"' type='button'>댓글삭제</button>  "
                str += "<button class='updateReview float-right' onclick='updateReview(event)'name='"+list.id+"' type='button'>댓글수정</button></li><hr/>";
            });
            $(".chatReview").html(str);
        }else {
            console.log("등록된댓글이 없습니다.");
        }
    }

    function errFunc1(e) {
    }

    $(document).ready(function(){
        getListReview();
    })

    // //댓글 추가하기
    // document.querySelector(".addReview").addEventListener("click", function (e) {
    //     e.preventDefault();
    //     var pno = document.querySelector(".pno").value;
    //
    //     var review = document.querySelector(".Reviewcontent").value;
    //     $.ajax({
    //         url: "/reply/shopping/addReview", // 요청 URL
    //         type: "post", // HTTP 메서드(전송방식)
    //         data: { // 매개변수로 전달할 데이터
    //             pno: pno,
    //             review: review, //검색어
    //             mid: 1,
    //             rating: 4
    //         },
    //         dataType: "json", // 응답 데이터 형식
    //         success: sucFuncJson, // 요청 성공 시 호출할 메서드 설정
    //         error: errFunc // 요청 실패 시 호출할 메서드 설정
    //     });
    // });

    // // 검색 성공 시 결과를 화면에 뿌려줍니다.
    // function sucFuncJson(result) {
    //     document.querySelector(".Reviewcontent").value = "";
    //     getList();
    // }
    //
    // function errFunc(e) {
    //     console.log("실패");
    // }
    // function updateReview1(event) {
    //     var targetButton = event.target;
    //     var parentListItem = targetButton.closest("li");
    //     var updateContentInput = parentListItem.querySelector(".updateContent");
    //
    //     // updateContentInput을 찾았는지 확인
    //     if (updateContentInput) {
    //         // updateContentInput을 사용하여 원하는 작업을 수행
    //         console.log("updateContentInput found:", updateContentInput);
    //         // 스타일 변경
    //         updateContentInput.style.display = "block";
    //     } else {
    //         console.log("updateContentInput not found");
    //     }
    // }

    // //댓글 수정하기
    //     function updateReview(event){
    //     var element = document.querySelector(".updateContent");
    //     console.log(element);
    //     // 스타일 변경
    //     element.style.display = "block";
    //     id = event.target.getAttribute("name");
    //     var content = document.querySelector(".Reviewcontent").value;
    //     // 선택된 요소 찾기
    //
    //     $.ajax({
    //         url: "/reply/shopping/update", // 요청 URL
    //         type: "put", // HTTP 메서드(전송방식)
    //         data: { // 매개변수로 전달할 데이터
    //             id: id,
    //             content: content, // 검색어
    //             answer: "답변",
    //             rating: 1.0,
    //             mid: 1
    //         },
    //         dataType: "json", // 응답 데이터 형식
    //         success: sucFuncJson2, // 요청 성공 시 호출할 메서드 설정
    //         error: errFunc2 // 요청 실패 시 호출할 메서드 설정
    //     });
    // }
    //
    // // 검색 성공 시 결과를 화면에 뿌려줍니다.
    // function sucFuncJson2(result) {
    //     console.log("성공2");
    //     getListReview();
    // }
    //
    // function errFunc2(e) {
    //     console.log("실패2");
    // }

    // //댓글 삭제하기
    // document.querySelector(".deleteReview").addEventListener("click", function (e) {
    //     e.preventDefault();
    //     var id = document.querySelector(".reviewId").value;
    //     console.log("id is " + id);
    //
    //     $.ajax({
    //         url: "/reply/shopping/delete", // 요청 URL
    //         type: "delete", // HTTP 메서드(전송방식)
    //         data: { // 매개변수로 전달할 데이터
    //             id: id
    //         },
    //         dataType: "json", // 응답 데이터 형식
    //         success: sucFuncJson3, // 요청 성공 시 호출할 메서드 설정
    //         error: errFunc3 // 요청 실패 시 호출할 메서드 설정
    //     });
    // });
    //
    // // 검색 성공 시 결과를 화면에 뿌려줍니다.
    // function sucFuncJson3(result) {
    //     console.log("성공3");
    // }
    //
    // function errFunc3(e) {
    //     console.log("실패3");
    // }




})
