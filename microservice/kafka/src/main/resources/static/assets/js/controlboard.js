// WebSocket 연결
var socket = new SockJS('/ws');
var stompClient = Stomp.over(socket);

stompClient.connect({}, function (frame) {
    console.log('Connected: ' + frame);
    // 메시지를 받을 주제 구독
    stompClient.subscribe('/api/kafka/data-flow', function (messageOutput) {
        // 서버에서 받은 메시지 처리
        var message = messageOutput.body;
        document.getElementById('messageArea').innerHTML = message; // 메시지 표시

        var logMessage = 'Kafka Sink Connector에 의해 게시글이 DB에 저장되었습니다.';  // 로그 메시지
        document.getElementById('messageArea').innerHTML += '<br><br>' + logMessage;  // 화면에 추가
        getBoards();

    });
});

$(document).ready(function() {
    $.ajaxSetup({
        // JSON 파싱 시 큰 숫자를 문자열로 처리
        converters: {
            'text json': function(response) {
                return JSON.parse(response, (key, value) => {
                    // bseq 필드를 문자열로 유지
                    if (key === 'bseq' && typeof value === 'number') {
                        return value.toString();
                    }
                    return value;
                });
            }
        }
    });

    getBoards();
});

//화면에 로그 표시
function displayLog(message) {
    const logArea = document.getElementById('logArea');
    logArea.innerHTML = "";
    const logMessage = document.createElement('p');
    logMessage.textContent = message;
    logArea.appendChild(logMessage);
}
// 게시판 목록을 불러오는 함수
function getBoards() {
    $.ajax({
        url: '/api/boards/list',
        method: 'GET',
        dataType: 'text',
        success: function(response) {
            const boardList = JSON.parse(response).data;
            let boardHtml = '';
            boardList.forEach(board => {
                boardHtml += `<li>
                                        ${board.title} - ${board.contents}
                                        <button data-bseq="${board.bseq}" onclick="deleteBoard(this)">Del</button>
                                       </li>`;
            });
            $('#boardList').html(boardHtml);
            console.log('DB에서 게시글 리스트가 반환되었습니다.');
        },
        error: function(error) {
            alert('게시판 목록을 불러오는 데 실패했습니다.');
        }
    });
}

// 게시물 추가 함수
$('#createForm').on('submit', function(event) {
    event.preventDefault();

    const boardData = {
        title: $('#title').val(),
        contents: $('#content').val()
    };

    $.ajax({
        url: '/api/boards/create',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(boardData),
        success: function(response) {
            const generatedBseq = response.data;
            alert('게시물이 추가되었습니다.');
            displayLog('DB에 게시글이 등록되었습니다.');
            getBoards();  // 목록을 새로고침
        },
        error: function(error) {
            alert('게시물 추가에 실패했습니다.');
        }
    });
});

// 게시물 삭제 함수
function deleteBoard(buttonElement) {
    const bseq = $(buttonElement).data('bseq');

    $.ajax({
        url: `/api/boards/delete/${bseq}`,
        method: 'DELETE',
        contentType: 'text/plain',
        success: function(response) {
            alert('게시물이 삭제되었습니다.');
            getBoards();  // 목록을 새로고침
            displayLog('DB에 게시글이 삭제되었습니다.');
        },
        error: function(error) {
            alert('게시물 삭제에 실패했습니다.');
        }
    });
}

// Kafka 게시물 추가 함수
$('#kafka-createForm').on('submit', function(event) {
    event.preventDefault();

    const boardData = {
        title: $('#kafka-title').val(),
        contents: $('#kafka-content').val()
    };

    $.ajax({
        url: '/api/kafka/board',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(boardData),
        success: function(response) {
            const generatedBseq = response.data;
            alert('게시물이 추가되었습니다.');
            displayLog('Kafka Topic에 Message가 등록되었습니다.');
            getBoards();  // 목록을 새로고침
        },
        error: function(error) {
            alert('Kafka Topic에 Message등록이 실패했습니다.');
        }
    });
});