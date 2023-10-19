    let answers = []
    let questionId
    let aid

     $('#questionDialog').on('shown.bs.modal', function (event) { // при открытии модального окна
         let button = $(event.relatedTarget)
         questionId = button.data('questionid')
         console.log(questionId)
         let modal = $('#questionDialog')
                           if (questionId) { // если редактируем вопрос
                           $('#questionDialogLabel').text("Редактирование вопроса")
                               $.get({
                                   url: '/api/v1/question/' + questionId,
                                   success: (data) => {
                                       let modal = $(this)
                                       modal.find('#question-id').val(data.id)
                                       modal.find('#question-name').val(data.text)
                                       modal.find('#question-ball').val(data.ball)
                                       modal.find('#' + data.typeAnswerOptions).prop("checked", true)
                                      if(data.type) modal.find('#selectTypeCreate').val(data.type.id)


                                       answers = data.answerList
                                       let ol = $('#listAnswers')
                                        for(var i =0; i<answers.length;i++){

                                                        if(answers[i].corrected)
                                                            ol.append('<li class="list-group-item active" aria-current="true">' + answers[i].text + '</li>');
                                                        else
                                                            ol.append('<li class="list-group-item">' + answers[i].text + '</li>');
                                        }
                                   },
                                   error: (err) => {
                                       alert(err);
                                   }
                               });
                           }
                           else $('#questionDialogLabel').text("Добавление вопроса")
                       })
     $('#saveAnswerButton').click(function() { //Сохраниене вариантов ответа
         let modal = $('#questionDialog')
         let ol = $('#listAnswers')
            let answer = {
                id: aid,
                text: modal.find('#question-newAnswer').val(),
                corrected: modal.find('#corrected').prop('checked'),
            }
            answers.push(answer)

            if(answer.corrected)
                ol.append('<li class="list-group-item active"  aria-current="true">' + answer.text + '</li>');
            else
                ol.append('<li class="list-group-item">' + answer.text + '</li>');
         })

        $('#saveQuestionButton').click(function() { //Создание нового вопроса
               let modal = $('#questionDialog')
               var textQuestion = modal.find('#question-name').val()
               var ballQuestion = modal.find('#question-ball').val()

               var valid = true;
                    if(!textQuestion){ //валидация
                             $('#validNameQuestion').removeAttr("hidden")
                             valid = false;
                       }
                       else  {
                       $('#validNameQuestion').prop("hidden", true)
                         valid = true;
                       }
                       if(ballQuestion<1){
                             $('#validBallQuestion').removeAttr("hidden")
                             valid = false;

                        }
                        else  {
                        $('#validBallQuestion').prop("hidden", true)
                          valid = valid;
                        }

               var typeAnswerOptions
               var oneAnswerRadio = modal.find('#ONE_ANSWER');
               var manyAnswerRadio = modal.find('#MANY_ANSWER');
               var freeAnswerRadio = modal.find('#FREE_ANSWER');

               if  (oneAnswerRadio.is(':checked')) typeAnswerOptions = oneAnswerRadio.val();
               if  (manyAnswerRadio.is(':checked')) typeAnswerOptions = manyAnswerRadio.val();
               if  (freeAnswerRadio.is(':checked')) typeAnswerOptions = freeAnswerRadio.val();
               var typeId = $('#selectTypeCreate option:selected').val()
               let question = {
                   id: questionId,
                   text: textQuestion,
                   ball: ballQuestion,
                   answerList: answers,
                   typeAnswerOptions: typeAnswerOptions,
               };
               let form = $('#formTest')
               let nameTest = form.find('#form3Example1c').val()


               if(oneAnswerRadio.is(':checked')){  //валидация типа варианта ответа
                   var count = 0;
                        for(var i = 0; i < answers.length; i++){
                            if (answers[i].corrected) count++;
                        }
                   if (count!=1){
                     $('#validAnswers').removeAttr("hidden")
                     return
                   }
                   else{
                     $('#validAnswers').prop("hidden", true)
                   }
               }
               if(manyAnswerRadio.is(':checked')){
                  var count = 0;
                       for(var i = 0; i < answers.length; i++){
                           if (answers[i].corrected) count++;
                       }
                       console.log(count)
                  if (count <= 1){
                    $('#validAnswers').removeAttr("hidden")
                    return
                  }
                  else{
                    $('#validAnswers').prop("hidden", true)
                  }
              }
               if(freeAnswerRadio.is(':checked')){
                 var count = 0;
                      for(var i = 0; i < answers.length; i++){
                          if (answers[i].corrected) count++;
                      }
                 if (count ==0){
                   $('#validAnswers').removeAttr("hidden")
                   return
                 }
                 else{
                   $('#validAnswers').prop("hidden", true)
                 }
                }
                console.log(answers)
                   $.ajax({
                       url: '/api/v1/question?typeId=' + typeId,
                       type: 'POST',
                       data: JSON.stringify(question),
                       contentType: "application/json; charset=utf-8",
                       dataType: "json",
                     success: () => {
                        location.reload()
                    },
                    error: (err) => {
                        console.log(err);
                    }

                   })
            modal.modal('hide');
       });
       $("#questionDialog").on("hidden.bs.modal", function () {
             $('#listAnswers').empty()
             $('.form-control').val('')
             $('#selectTypeCreate').val("-1")
             answers = []
             $('#ONE_ANSWER').prop("checked", true)
       });

       $('.deleteButton').click(function() {
        console.log()
         $.ajax({
                               url: '/api/v1/question/delete/' + $(this).attr('value'),
                               type: 'DELETE',
                             success: () => {
                                location.reload()
                            },
                            error: (err) => {
                                console.log(err);
                            }

                           })
       })
