(function($){ 
	$.fn.badPass = "Fraca";
	$.fn.goodPass = "OK";
	$.fn.strongPass = "Forte";
	$.fn.resultStyle = "";
	
	 $.fn.passStrength = function(options) {  
	  
		 var defaults = {
				badPass:		"badPass",
				goodPass:		"goodPass",
				strongPass:		"strongPass",
				baseStyle:		"testresult"
			}; 
		 	var opts = $.extend(defaults, options);  

		 	return this.each(function() { 
		 		 var obj = $(this);
		 		
		 		var div = $("<div>");
		 		$(obj).after(div);
		 		$(obj).appendTo(div);
		 		$(obj).css("float", "left");
		 		
		 		var span = $("<span class=\""+opts.baseStyle+"\">");
		 		$(obj).after(span);
		 		
		 		var divClear = $("<div style='clear:both'>");
		 		span.after(divClear);
		 		
		 		$(obj).unbind().keyup(function() {					
					var results = $.fn.teststrength($(this).val(), opts);
					
					span.removeClass("badPass")
						.removeClass("goodPass")
						.removeClass("strongPass")
						.addClass($(this).resultStyle)
						.text(results);
		 		});
		 		 
		 		// FUNCTIONS
		 		$.fn.teststrength = function(password, option){

		 			// Se a senha tem mais 8+ caracteres, uma letra e um número, ela é aceitável ...
	 			    if (password.length >= 8 && password.match(/([a-zA-Z])/) && password.match(/([0-9])/)) {

		 			    // Se ela tem um símbolo e mistura maiúsculas e minúsculas, é forte ...
		 			    if (password.match(/(.*[!,@,#,$,%,^,&,*,?,_,~])/) && password.match(/([a-z].*[A-Z])|([A-Z].*[a-z])/))
	 			    	{
		 			    	this.resultStyle = option.strongPass;
					    	return $(this).strongPass;
	 			    	}
		 			    
	 			    	this.resultStyle = option.goodPass;
				    	return $(this).goodPass;
	 			    }

			    	this.resultStyle = option.badPass; 
			    	return $(this).badPass;
		 		};
		  });  
	 };  
})(jQuery);