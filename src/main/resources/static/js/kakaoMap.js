/*map 기본설정----------------------------------------------------*/

//사용자 정보를 통해 기본주소 불러와서 기본 주소를 마커로 표시해주기.->필요
var container = document.getElementById('map');
var options = {
    center: new kakao.maps.LatLng(37.481001, 126.952312),
    level: 3
};

var map = new kakao.maps.Map(container, options);

// 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
var mapTypeControl = new kakao.maps.MapTypeControl();

// 지도 타입 컨트롤을 지도에 표시합니다
map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

// 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
var zoomControl = new kakao.maps.ZoomControl();
map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
//---------------------------------------------------------------------------
var markers = [];

function removeMarker() {
    for ( var i = 0; i < markers.length; i++ ) {
        markers[i].setMap(null);
    }
    markers = [];
}

function searchMountain(result){
    removeMarker();
    // 지도를 재설정할 범위정보를 가지고 있을 LatLngBounds 객체를 생성합니다
    var bounds = new kakao.maps.LatLngBounds();
    /*map 기본설정.END----------------------------------------------------*/
    var points = [];
    var infowindows = [];
    $(result).each(function(){
        var point = {
            content : "<a href='/community/board?id="+this.id+"'>"+this.name+" 커뮤니티</a>",
            latlng : new kakao.maps.LatLng(this.lat, this.lng)
        };
        // 배열의 좌표들이 잘 보이게 마커를 지도에 추가합니다
        var marker = new kakao.maps.Marker({
            map: map,
            position: point.latlng
        });

        // 마커에 표시할 인포윈도우를 생성합니다
        var infowindow = new kakao.maps.InfoWindow({
            content: point.content, // 인포윈도우에 표시할 내용
            removable: true
        });
        points.push(point);
        markers.push(marker);
        infowindows.push(infowindow);
    });

    for (let i = 0; i < points.length; i++) {

        // LatLngBounds 객체에 좌표를 추가합니다
        bounds.extend(points[i].latlng);

        // 마커에 mouseover 이벤트와 mouseout 이벤트를 등록합니다
        // 이벤트 리스너로는 클로저를 만들어 등록합니다
        // for문에서 클로저를 만들어 주지 않으면 마지막 마커에만 이벤트가 등록됩니다
        kakao.maps.event.addListener(markers[i], 'click', clickListener(map, markers[i], infowindows[i]));
    }
    // LatLngBounds 객체에 추가된 좌표들을 기준으로 지도의 범위를 재설정합니다
    // 이때 지도의 중심좌표와 레벨이 변경될 수 있습니다
    map.setBounds(bounds);
}

// 인포윈도우를 클릭했을때 함수입니다
function clickListener(map, marker, infowindow) {
    return function () {
        infowindow.open(map, marker);
    };
}

//검색시 ajax 로 요청하여 정보 띄워주고 성공하면 정보 지도로 넘겨서 지도 새로 마커표시해주기.
document.querySelector(".searchButton").addEventListener("click", function (e) {
    e.preventDefault();
    var keyword =  document.querySelector(".searchValue").value;
    $.ajax({
        url: "/community/searchKeyword", // 요청 URL
        type: "get", // HTTP 메서드(전송방식)
        data: { // 매개변수로 전달할 데이터
            keyword: keyword, // 검색어
            pageNum : 0
        },
        dataType: "json", // 응답 데이터 형식
        success: sucFuncJson, // 요청 성공 시 호출할 메서드 설정
        error: errFunc // 요청 실패 시 호출할 메서드 설정
    });
});

function searchKeywordforPage(keyword,pageNum){
    $.ajax({
        url: "/community/searchKeyword", // 요청 URL
        type: "get", // HTTP 메서드(전송방식)
        data: { // 매개변수로 전달할 데이터
            keyword: keyword, // 검색어
            pageNum : pageNum   //페이지 번호.
        },
        dataType: "json", // 응답 데이터 형식
        success: sucFuncJson, // 요청 성공 시 호출할 메서드 설정
        error: errFunc // 요청 실패 시 호출할 메서드 설정
    });
}

// 검색 성공 시 결과를 화면에 뿌려줍니다.
function sucFuncJson(result) {
    //결과값으로 지도에 마커 표시하기.
    searchMountain(result.content);
    //결과 값으로 밑에 검색결과 띄워주기
    //페이징 처리 필요
    var str = "";
    $(result.content).each(function(){
        str += " <tr>";
        str += "        <td>"+this.name+"</td>";
        str += "        <td>"+this.address+"</td>";
        str += "        <td><a href='/community/board?id="+this.id+"'><button class='btn btn-outline-secondary' type='button'>커뮤니티 이동</button></a></td>";
        str += " <" +
            "/tr>";
    });
    $('#mapInformResult').html(str);
    showReplyPage(result.totalElements, result.number+1); //페이징 처리(총 갯수, 페이지번호->자바는 0부터 시작 +1필요.)
}
//페이징 처리 된 번호 보여주기.
function showReplyPage(replyCnt,pageNum){

    var endNum=Math.ceil(pageNum/5.0)*5; // 1.2.3 ... 10에서 10
    var startNum=endNum-4; // 1.2.3 ... 10에서 1. 10-9==1
    var prev=startNum!=1; //previous유무
    var next=false; //next유무
    // 댓글갯수가 endNum*10보다 작을 때(실제끝페이지가 계산으로 구한 마지막페이지보다 작을 때)
    if(endNum*5>=replyCnt){
        endNum=Math.ceil(replyCnt/5.0);//마지막페이지 변경
    }
    // 댓글갯수가 endNum*10보다 크면 next존재
    if(endNum*5<replyCnt){
        next=true;
    }
    var str="<ul class='pagination justify-content-end mt-50'>";


    if(prev){
        str+="<li class='page-item'><a class='page-link' href='"+(startNum-1)+"'><<</a></li>";
    }
    for(var i=startNum;i<=endNum;i++){
        var active=pageNum==i?"active":""; // page번호가 현재페이지 이면 active 설정
        str+="<li class='page-item "+active+"'><a class='page-link' href='"+i+"'>"+i+"</a></li>";
    }
    if(next){
        str+="<li class='page-item'><a class='page-link' href='"+(endNum+1)+"'>>></a></li>";
    }
    str+=" </ul>";
    $("#pagination").html(str);
}

// 실패 시 경고창을 띄워줍니다.
function errFunc(e) {
    var str = "";
        str += " <tr>";
        str += "        <td></td>";
        str += "        <td><h6>검색결과가 없습니다. 다시 검색해주세요.</h6></td>";
        str += "        <td></td>";
        str += " </tr>";
    $('#mapInformResult').html(str);
}

$(document).ready(function(){

    $("#pagination").on("click","li a", function(e){
        e.preventDefault();		//다른페이지 이동 방지.
        var pageNum = $(this).attr("href");
        var keyword = document.querySelector(".searchValue").value;
        searchKeywordforPage(keyword,pageNum-1);
    });

});
