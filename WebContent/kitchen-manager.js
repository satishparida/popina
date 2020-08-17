function loadAllPendingOrders(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var orders = JSON.parse(this.responseText);
			
			var html = '';
			var orderId = '';
			var orderStatus= '';
			var productName= '';
			var quantity = '';
		    
			for (order in orders) {
				orderId = orders[order].orderid;
				orderStatus = orders[order].orderstatus;
				
				html = '<div id="div' + orderId + '">';
				html += '<form id="form' + orderId + '">';
				html += '<input type="hidden" name="id" value="' + orderId + '" />';
				
				for (item in orders[order].items){
					productName=orders[order].items[item].productname;
					quantity=orders[order].items[item].quantity;
					
					html += '<br>Name: ' + productName + '<br>Quantity: ' + quantity + ' <br>';
				}
				
				statusFlag = '';
				html = '<select name="status">';
				if (orderStatus='order placed'){ statusFlag='selected';} else {statusFlag = '';}
				html += '<option value="order placed" '+statusFlag+'>order placed</option>';
				if (orderStatus='being prepared'){ statusFlag='selected';} else {statusFlag = '';}
				html += '<option value="being prepared" '+statusFlag+'>being prepared</option>';
				if (orderStatus='ready for collection'){ statusFlag='selected';} else {statusFlag = '';}
				html += '<option value="ready for collection" '+statusFlag+'>ready for collection</option>';
				if (orderStatus='collected'){ statusFlag='selected';} else {statusFlag = '';}
				html += '<option value="collected" '+statusFlag+'>collected</option>';
				html += '</select>';
				html += '<input type="button" value="Update" onClick="updateOrderStatus(' + orderId + ');">';
				html += '</form> </div>';
				
			}
			var content = document.getElementById("all_orders");
		    content.innerHTML = html;
		}
		else if (this.status == 404){
			
		}
	}
	request.open("GET","kitchenmanager",true);
	request.send();
}

function sendUpdate(id) {
	var form = document.getElementById("form"+id);
    var status = form.elements["status"].value;
    var message = { "id" : id, "status" : status };
    socket.send(JSON.stringify(message));
}