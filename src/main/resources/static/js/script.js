// console.log("script loaded...");

let cuurentTheme = getTheme();
document.addEventListener('DOMContentLoaded',()=>{
    changeTheme();
});

function changeTheme() {
    //set to web page
    changePageTheme(cuurentTheme, "");
    //set listener to handle button
    const changeThemeButton = document.querySelector("#theme_change_button");

    changeThemeButton.addEventListener("click", (event) => {
        let oldTheme = cuurentTheme;
        // console.log("button clicked");
        if (cuurentTheme == "dark") {
            cuurentTheme = "light";
        } else {
            cuurentTheme = "dark";
        }
        changePageTheme(cuurentTheme, oldTheme);
    });
}

//set theme to local storage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

//get theme from local storage
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "dark";
}

function changePageTheme(theme, oldTheme) {
    setTheme(theme);
    if(oldTheme){
    document.querySelector("html").classList.remove(oldTheme);
    }
    document.querySelector("html").classList.add(theme);
    document.querySelector("#theme_change_button").querySelector("span").textContent = cuurentTheme == "light" ? "Dark" : "Light";
}