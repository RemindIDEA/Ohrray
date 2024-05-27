

//댓글 가져오기
function getList(){
    var bid = document.querySelector(".bid").value;
    console.log(bid);
    $.ajax({
        url: "/reply/community/getList", // 요청 URL
        type: "get", // HTTP 메서드(전송방식)
        data: { // 매개변수로 전달할 데이터
            bid : bid // 부모글번호
        },
        dataType: "json", // 응답 데이터 형식
        success: sucFuncJson1, // 요청 성공 시 호출할 메서드 설정
        error: errFunc1 // 요청 실패 시 호출할 메서드 설정
    });
}

function sucFuncJson1(result) {
    if (result){
        var str = "";
        var midValue = document.getElementById('mid').value;
        var memValue = document.getElementById('mem').value;
        console.log(midValue);
        console.log(memValue);

        // 날짜 형식을 yyyy-mm-dd hh:mm으로 변환하는 함수
        function formatDate(dateString) {
            var date = new Date(dateString);
            var year = date.getFullYear();
            var month = (date.getMonth() + 1).toString().padStart(2, '0');
            var day = date.getDate().toString().padStart(2, '0');
            var hours = date.getHours().toString().padStart(2, '0');
            var minutes = date.getMinutes().toString().padStart(2, '0');
            return `${year}-${month}-${day} ${hours}:${minutes}`;
        }

        result.forEach(function(list, index) {
            var memid = list.mid;
            console.log(list.regDate);
            // 날짜 형식을 변환
            var formattedDate = formatDate(list.regDate);
            str +="<li class='left clearfix' data-rno='"+list.id+"'>";
            str +="  <div><div class='header'><strong class='primary-font'>["
                +list.id+"] "+list.mid+"</strong>";
            str +="    <small class='pull-right text-muted'>"
                +formattedDate+"</small></div>";
            str +="    <p>"+list.content+"</p></div>";
            if(memValue == memid){
                str +=" <button class='deleteReply float-right btn btn-outline-secondary' type='button'>댓글삭제</button>  "
                str +=  "<button class='updateReply float-right btn btn-outline-secondary' type='button'>댓글수정</button></li>";
            }
            str+="<hr/>";
        });
        $(".chat").html(str);

    }
}
function errFunc1(e) {
}

//댓글 추가하기
document.querySelector(".addReply").addEventListener("click", function (e) {
    e.preventDefault();
    var bid = document.querySelector(".bid").value;
    console.log(bid);
    var content =  document.querySelector(".replycontent").value;
    console.log(content);
    $.ajax({
        url: "/reply/community/add", // 요청 URL
        type: "post", // HTTP 메서드(전송방식)
        data: { // 매개변수로 전달할 데이터
            bid : bid,
            content : content, // 검색어
        },
        dataType: "json", // 응답 데이터 형식
        success: sucFuncJson, // 요청 성공 시 호출할 메서드 설정
        error: errFunc // 요청 실패 시 호출할 메서드 설정
    });
});

// 검색 성공 시 결과를 화면에 뿌려줍니다.
function sucFuncJson(result) {
    console.log("성공");
    document.querySelector(".replycontent").value="";
    getList();
}
function errFunc(e) {
    console.log("실패");
}


//댓글 수정하기
$(".chat").on("click", ".updateReply", function(e) {
    e.preventDefault();

    // 클릭된 버튼을 this로 참조
    var clickedButton = $(this);

    // 버튼의 상위 요소 중 .left.clearfix 클래스 요소를 찾아 data-rno 값을 가져옴
    var listItem = clickedButton.closest('.left.clearfix');
    var id = listItem.data('rno');

    // 콘솔에 출력
    console.log(id);

    // 입력된 내용 가져오기 (이 부분은 각 상황에 맞게 수정 필요)
    var content = prompt("수정할 내용을 입력하세요:", listItem.find("p").text());
    console.log(content);
    if (content === null) return; // 취소를 누른 경우
    $.ajax({
        url: "/reply/community/update", // 요청 URL
        type: "put", // HTTP 메서드(전송방식)
        data: { // 매개변수로 전달할 데이터
            id : id,
            content : content, // 검색어
        },
        dataType: "json", // 응답 데이터 형식
        success: sucFuncJson2, // 요청 성공 시 호출할 메서드 설정
        error: errFunc2 // 요청 실패 시 호출할 메서드 설정
    });
});



// 검색 성공 시 결과를 화면에 뿌려줍니다.
function sucFuncJson2(result) {
    console.log("성공2");
    getList();
}
function errFunc2(e) {
    console.log("실패2");
    alert("다시 시도해주세요.");
}

//댓글 삭제하기
$(".chat").on("click", ".deleteReply", function(e) {
    e.preventDefault();

    // 클릭된 버튼을 this로 참조
    var clickedButton = $(this);

    // 버튼의 상위 요소 중 .left.clearfix 클래스 요소를 찾아 data-rno 값을 가져옴
    var listItem = clickedButton.closest('.left.clearfix');
    var id = listItem.data('rno');

    // 콘솔에 출력
    console.log(id);

    $.ajax({
        url: "/reply/community/delete", // 요청 URL
        type: "delete", // HTTP 메서드(전송방식)
        data: { // 매개변수로 전달할 데이터
            id : id
        },
        dataType: "text", // 응답 데이터 형식
        success: sucFuncJson3, // 요청 성공 시 호출할 메서드 설정
        error: errFunc3 // 요청 실패 시 호출할 메서드 설정
    });
});

// 검색 성공 시 결과를 화면에 뿌려줍니다.
function sucFuncJson3(result) {
   location.reload();
    getList();
}
function errFunc3(e) {
    alert("처리중에 이상이 생겼습니다. 다시 시도해주세요.");
}

$(document).ready(function(){
    getList();
})


