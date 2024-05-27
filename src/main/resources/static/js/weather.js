//당일 날짜 구하기.
var today = new Date();
var year = today.getFullYear();
var month = ('0' + (today.getMonth() + 1)).slice(-2);
var day = ('0' + today.getDate()).slice(-2);
var dayString = year+''+month+''+day;

var hour = today.getHours()+'00';

/* Javascript 샘플 코드 */
var xhr = new XMLHttpRequest();
var url = 'http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst'; /*URL*/
var queryParams = '?' + encodeURIComponent('serviceKey') + '='+'NHPFmbr7SlDzrf7BOlNjaE47MFmSoy4gnbNMonz9u9nlsYAfuccnBD3ohL5xMAcSiS87dU%2BuYvNJG6XHImiPtA%3D%3D'; /*Service Key*/
queryParams += '&' + encodeURIComponent('pageNo') + '=' + encodeURIComponent('1'); /**/
queryParams += '&' + encodeURIComponent('numOfRows') + '=' + encodeURIComponent('12'); /**/
queryParams += '&' + encodeURIComponent('dataType') + '=' + encodeURIComponent('JSON'); /**/
queryParams += '&' + encodeURIComponent('base_date') + '=' + encodeURIComponent(dayString); /**/
queryParams += '&' + encodeURIComponent('base_time') + '=' + encodeURIComponent(hour); /**/
queryParams += '&' + encodeURIComponent('nx') + '=' + encodeURIComponent('37'); /**/
queryParams += '&' + encodeURIComponent('ny') + '=' + encodeURIComponent('126'); /**/
xhr.open('GET', url + queryParams);
xhr.onreadystatechange = function () {
    if (this.readyState == 4) {
        //console.log(this.status);
        //console.log(JSON.stringify(this.getAllResponseHeaders()));
        //console.log(this.responseText);
        obj = JSON.parse(this.responseText);
    }
};
xhr.send('');
