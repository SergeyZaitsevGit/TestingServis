
//кнопка поиска
    $('#search-question').click(function() {
        var questionsIds = [];
        allQuestionHtml = $('.accordion-item');
        typeId = $('#selectTypeSearch').val();
        if (typeId == "-1") {
         allQuestionHtml.show();
         return
    }
          $.get({ //гет запрос на получения списка вопросов по id
                        url: '/api/v1/question/type/' + typeId,
                        success: (data) => {
                            questionsIds = data;
                                     for(var i = 0;i<allQuestionHtml.length; i++){
                                        allQuestionHtml.eq(i).hide();

                                            for(var j = 0;j<questionsIds.length; j++){
                                                    if (allQuestionHtml.eq(i).attr('value') == questionsIds[j]){
                                                        allQuestionHtml.eq(i).show();
                                                    }
                                                }
                                            }

                        },
                        error: (err) => {
                            alert(err);
                        }
                    });
    });