    let answers = []
     $('#save-answer-button').click(function() { //Сохраниене вариантов ответа
         let modal = $('#userEditDialog')
         let ol = $('#listAnswers')
            let answer = {
                text: modal.find('#question-newAnswer').val(),
                corrected: modal.find('#corrected').prop('checked'),
                question: null,
            }
            answers.push(answer)
            if(answer.corrected)
                ol.append('<li class="list-group-item active">' + answer.text + '</li>');
            else
                ol.append('<li class="list-group-item">' + answer.text + '</li>');
         })

        $('#save-user-button').click(function() { //Создание нового вопроса
               let modal = $('#userEditDialog')
               var textQuestion = modal.find('#question-name').val()
               var ballQuestion = modal.find('#question-ball').val()
                    if(!textQuestion){
                             $('#validNameQuestion').removeAttr("hidden")

                       }
                       else  {$('#validNameQuestion').prop("hidden", true)}
                       if(ballQuestion<1){
                             $('#validBallQuestion').removeAttr("hidden")

                        }
                        else  {$('#validBallQuestion').prop("hidden", true)}

               let option = $('#selectTypeCreate option:selected')
               let question = {
                   text: textQuestion,
                   ball: ballQuestion,
                   answerList: answers,
               };
               let form = $('#formTest')
               let nameTest = form.find('#form3Example1c').val()

                   $.ajax({
                       url: '/api/v1/question?typeId=' + option.val(),
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