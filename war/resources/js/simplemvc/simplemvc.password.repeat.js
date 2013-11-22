(function($){ 
	$.fn.diffPass = "Diferente";
	$.fn.equalPass = "Igual";
	$.fn.resultStyle = "";
	
	$.fn.passRepetition = function(options) {  
	  
		 var defaults = {
				source: 		"",
				diffPass:		"diffPass",
				equalPass:		"equalPass",
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
		 		
		 		$(obj).unbind().keyup(function()
		 		{					
					var results = $.fn.testrepeat($(this).val(), $(opts.source).val(), opts);
					
					span.removeClass("diffPass")
						.removeClass("equalPass")
						.addClass($(this).resultStyle)
						.text(results);
		 		 });
		 		 
		 		$.fn.testrepeat = function(password,sourcepwd,option){
		 			  if (password === sourcepwd)
		 			  { 
		 				  this.resultStyle = option.equalPass; 
		 				  return $(this).equalPass; 
		 			  }
		 			   
		 			  this.resultStyle = option.diffPass; 
		 			  return $(this).diffPass;
		 		};
		  });  
	 };  
})(jQuery);