/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
function stopRKey(evt) {
    alert("key pressed");
    var evt = (evt) ? evt : ((event) ? event : null);
    var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
    if ((evt.keyCode == 13) && (node.type=="text"))  {
        
        document.getElementById('loginButton').click();
        return false;
    }
}
//document.onkeypress = stopRKey;

