//검색 api -------------------------------------------------

// [검색 요청] 버튼 클릭 시 실행할 메서드를 정의합니다.
function search(result) {
    var data = result
    $("#searchSection").show();
    $.ajax({
        url: "/community/search", // 요청 URL
        type: "get", // HTTP 메서드(전송방식)
        data: { // 매개변수로 전달할 데이터
            keyword: data, // 검색어
        },
        dataType: "json", // 응답 데이터 형식
        success: sucFuncJson, // 요청 성공 시 호출할 메서드 설정
        error: errFunc // 요청 실패 시 호출할 메서드 설정
    });
}


// 검색 성공 시 결과를 화면에 뿌려줍니다.
function sucFuncJson(d) {
    var str = "";
    $.each(d.items, function (index, item) {
        str += "<div class='mb-4'>";
        str += "    <a class='link-dark' href='" + item.link + "'><h3>" + item.title + "</h3></a>";
        str += "    <div class='small text-muted'>" + item.postdate + "</div>";
        str += "    <p>*" + item.description + "</p>"
        str += "    <p>작성자 : " + item.bloggername + "</p>"
        str += "</div>";
    });
    $('#searchResult').html(str);
}

// 실패 시 경고창을 띄워줍니다.
function errFunc(e) {
    alert("실패: " + e.status);
}
//검색 api.end-------------------------------------------------