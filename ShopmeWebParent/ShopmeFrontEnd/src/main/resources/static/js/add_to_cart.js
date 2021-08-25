$(document).ready(function(){
	$("#buttonAdd2Cart").on("click",function(evt){
		addCart();
	});
	
});

function addCart(){
	quantity = $("#quantity"+productId);
	url = contextPath + "cart/add/" + productId + "/" + quantity.val();
	$.ajax({
		type: "POST",
		url: url,
		beforeSend : function(xhr){
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response){
		showModalDialog("Shopping Cart", response);
	}).fail(function(){
		showErrorModal("Error while adding product to shopping cart.");
	});
}