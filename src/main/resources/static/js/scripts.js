$('.answer-write').find('input[type="submit"]').on('click', function(e) {
	e.preventDefault();
	
	var url = $('.answer-write').attr('action');
	var queryString = $('.answer-write').serialize();
	
	console.log('url: ' + url);
	console.log('queryString: ' + queryString);
	
	$.ajax({
		type: 'post',
		url: url,
		data: queryString,
		dataType: 'json',
		error: onError,
		success: onSuccess
	});
});

$('a.link-delete-article').on('click', function(e) {
	e.preventDefault();
	
	var that = $(this);
	var url = that.attr('href');
	
	$.ajax({
		type: 'delete',
		url: url,
		dataType: 'json',
		error: function(xhr, status) {
			console.log('error')
		},
		success: function(data, status) {
			if ( !data.valid ) {
				alert(data.errorMessage);
				return;
			}
			
			that.closest('article').remove();
		}
	});
});

function onError() {
	
}

function onSuccess(data, status) {
	console.log(data);
	
	var answerTemplate = $('#answerTemplate').html();
	//var template = answerTemplate.format(data.writer.userId, data.formattedRegdate, data.contents, data.question.id, data.id);
	var template = answerTemplate.format(data.writer.userId, data.formattedRegdate, data.contents, data.question.id, data.id);
	
	$('.qna-comment-slipp-articles').find('article').last().append(template);
	
	$('.answer-write').find('textarea').val('');
}

String.prototype.format = function() {
	var args = arguments;
	
	return this.replace(/{(\d+)}/g, function(match, number) {
		return typeof args[number] != 'undefined'
			? args[number]
			: match
			;
	});
};