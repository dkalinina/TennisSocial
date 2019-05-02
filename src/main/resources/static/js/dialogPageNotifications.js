function setButtonsFunctions(user) {
    $("#sendNewMessage").click(function(event) {
        $.post("/dialog?user="+user,
            { message: $("#newMessage").val() },
            function(m) {
                $("#newMessage").val("");
                $("#messagesArea").append(m);
            });
    });
}

//на этой странице функция переопределена
function printMessage(message, user) {
    if (message.author.login != user) {
        //это сообщение для текущего пользователя, но для другой ветки диалогов
        addNotification(message.notification);
    } else {
        $("#messagesArea").append(message.htmlBlock);
    }
}

//на этой странице функция переопределена
function updateMatch(matchState, user) {
    if (matchState.author.login != user) {
        //это сообщение для текущего пользователя, но для другой ветки диалогов
        addNotification(matchState.notification);
    } else {
        $("#reaction").empty();
        $("#reaction").append(matchState.htmlBlock);
        setButtonsFunctions(user);
    }
}