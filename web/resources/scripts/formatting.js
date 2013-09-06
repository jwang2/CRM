
function whichKey (e) {
    var code; 
    if (window.event) {
        code = event.keyCode;
    }
    else {
        code = e.which;
    }
    //alert(code);
    return code;
}
            
function formatPhone (text, e) {
    var result, input;
    input = text.value;
                
    var keyId =  whichKey(e);
    if (keyId == 8 || keyId == 46){
        return;
    }
    if (input != "" )  {
        if (input.length < 5) {
            result = input.replace(/^\(?([0-9]{3})\)?$/, "($1) ");
        }
        else  if (input.length < 10) {
            result = input.replace(/^\(?([0-9]{3})\)?[-. ]?([0-9]{3})$/, "($1) $2-");
        }
        else if (input.length < 15)
        {
            result = input.replace(/^\(?([0-9]{3})\)?[-. ]?([0-9]{3})[-. ]?([0-9]{4})$/, "($1) $2-$3");
        }
        else {
            result = input.substring(0,14);
        }
        text.innerHTML=result;
        text.value=result;
        return result;
              
    }
    return;
};
            
function formatSSN (text, e) {
    var result, input;
    input = text.value;
    var keyId =  whichKey(e);
    if (keyId == 8 || keyId == 46){
        return;
    }
    if (input != "" )  {
        if (input.length < 4) {
            result = input.replace(/^\(?([0-9]{3})\)?$/, "$1-");
        }
        else  if (input.length < 7) {
            result = input.replace(/^\(?([0-9]{3})\)?[-. ]?([0-9]{2})$/, "$1-$2-");
        }
        else if (input.length < 12)
        {
            result = input.replace(/^\(?([0-9]{3})\)?[-. ]?([0-9]{2})[-. ]?([0-9]{4})$/, "$1-$2-$3");
        }
        else {
            result = input.substring(0, 11);
        }
        text.innerHTML=result;
        text.value=result;
        return result;
              
    }
    return;
};
            
function formatDate (text, e) {
    var result, input;
    input = text.value;
    var keyId =  whichKey(e);
    if (keyId == 8 || keyId == 46){
        return;
    }
                
    if (input != "" )  {
        input = text.value.replace ("//", "/");
        if (input.length < 4) {
            result = input.replace(/^([0-9]{2})$/, "$1/");
        }
        else  if (input.length < 7) {
            result = input.replace(/^([0-9]{2})[\/. ]([0-9]{2})$/, "$1/$2/");
        }
        else if (input.length < 11)
        {
            result = input.replace(/^([0-9]{2})[\/. ]([0-9]{2}) [\/. ]([0-9]{4})$/, "$1/$2/$3");
        }
        else {
            result = input.substring(0, 10);
        }
        text.innerHTML=result;
        text.value=result;
        return result;
              
    }
                
                
    return;
};
            
function formatMMYYDate (text, e) {
    var result, input;
    input = text.value;
    var keyId =  whichKey(e);
    if (keyId == 8 || keyId == 46){
        return;
    }
                
    if (input != "" )  {
        input = text.value.replace ("//", "/");
        if (input.length < 4) {
            result = input.replace(/^([0-9]{2})$/, "$1/");
        }
        else {
            result = input.substring(0, 5);
        }
        text.innerHTML=result;
        text.value=result;
        return result;
              
    }
                
                
    return;
};
            
            
function formatEIN (text, e) {
    var result, input;
    input = text.value.replace ("--", "-");
               
    var keyId =  whichKey(e);
    if (keyId == 8 || keyId == 46){
        return;
    }
    if (input != "" )  {
        if (input.length < 3) {
            result = input.replace(/^\(?([0-9]{2})\)?$/, "$1-");
        }
        else if (input.length < 11)
        {
            result = input.replace(/^\(?([0-9]{3})\)?[-. ]?([0-9]{2})[-. ]?([0-9]{4})$/, "$1-$2-$3");
        }
        else {
            result = input.substring(0, 10);
        }
        text.innerHTML=result;
        text.value=result;
        return result;
              
    }
    return;
};
             
function formatMoney (text) {
    var result, input;
    input =  text.value.replace (/[,]/g, "");
           
    if (input != "" )  {
        var sep = ",";
        var num= new Number(input).toFixed(2);
        //result = input.replace(/^[+-]?[0-9]{1,3}(?:,?[0-9]{3})*(?:\.[0-9]{2})?$/, "$1-$2-$3");
        result = num.replace(/(\d)(?=(\d\d\d)+(?!\d))/g,'$1'+ sep);
        text.innerHTML=result;
        text.value=result;
        return result;
              
    }
    else {
        return 0.00;
    }
};
            
function unCheck (box) {
               
    box.checked=false;
              
}
           
function reload () {
    qStr = Location.search;
       history.go(0);
    value=refresh;
}
var refresh = true;



