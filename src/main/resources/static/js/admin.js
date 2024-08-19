// console.log("admin log.");

document.querySelector("#contact_image_input").addEventListener('change',function(event){
    console.log("event fired.")
    let file = event.target.files[0];
    let reader = new FileReader();
    reader.onload = function(){
        document.getElementById("contact_img_preview").setAttribute("src",reader.result);
    };
    reader.readAsDataURL(file);
});