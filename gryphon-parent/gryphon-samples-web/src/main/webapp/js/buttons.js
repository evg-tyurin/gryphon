// берет выбранный объект и шлет на сервер
function submitForm(action) {
  var oid = -1;
  var oids = document.all.id;
  if (oids != null) {//alert('length='+oids.length);
    var i=0;
    if (oids.length != undefined) {
      for (i=0; i<oids.length; i++) {
        if (oids[i].checked) {
          oid = oids[i].value;
        }
      }// for
    }
    else {
      oid = oids.value;
    }
  }
  if (oid == -1) {
    alert('Ничего не выбрано. ');
    return;
  }
  // oid ready
  var f = document.all.myform;
  f.action = action;
  //alert('change from '+f.elements.oid.value+' to '+oid);
  f.elements.oid.value = oid;
  f.submit();
}
