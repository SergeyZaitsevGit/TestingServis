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
        ol.append('<li class="list-group-item">' + answer.text + ' ' + answer.corrected + '</li>');
     })

    $('#save-user-button').click(function() { //Создание нового вопроса
           let modal = $('#userEditDialog')
           let option = $('#selectTypeCreate option:selected')

           let question = {
               text: modal.find('#question-name').val(),
               ball: modal.find('#question-ball').val(),
               answerList: answers,
           };
           let form = $('#formTest')
           let nameTest = form.find('#form3Example1c').val()

           console.log(question);

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