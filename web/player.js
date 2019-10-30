//html DOM elements
let vlist = document.getElementsByTagName("li");
let player  = document.getElementById("player");
let vsource = document.getElementById("video-source");
//global flags
let video_loaded = false;

//media source object


for(let i = 0; i < vlist.length; i++){
    vlist[i].addEventListener("click",setSource);

}


function setSource(){
    for(let i = 0 ;i < vlist.length;i ++){
        vlist[i].style=null;
    }
    this.style = "background: rgb(20,20,20);";
    video_loaded = false;
    vsource.src=this.id;
    player.load();

}




