console.log("Loading menu-ui.js");



let userInfo;

function resetUserInfo() {
    userInfo = {
        authenticated : false,
        accessToken : '',
        name : 'Sign In',
        email : ''
    };
}
resetUserInfo();


function login() {
    console.log("login");
    if (!userInfo.authenticated) {
        console.log("Launching Amazon Login Authorize...");
        options = {scope: 'profile'};
        amazon.Login.authorize(options, handleAmazonAuthorize);
    } else {
        validate();
    }
}

function handleAmazonAuthorize(response) {
    console.log('onAuthorizeResponse');

    if (response.error) {
        reportError("authorize", response);
        return;
    }
    console.log("Access Token: " + response.access_token);

    userInfo.accessToken = response.access_token;
    userInfo.authenticated = true;

    validate();
}

function reportError(context, errorResponse) {
    console.log(context+' ' + errorResponse.error + ' - ' + errorResponse.error_description);
    alert(context+' ' + errorResponse.error + ' - ' + errorResponse.error_description);
}


function validate() {
    console.log("validate");

    console.log('Calling retrieveProfile');
    amazon.Login.retrieveProfile(userInfo.accessToken, function (response) {
        console.log('Profile Response:' + JSON.stringify(response));

        if (response.error) {
            reportError('profile', response);
            return;
        }

        userInfo.name = response.profile.Name;
        userInfo.email = response.profile.PrimaryEmail;

        refreshUi();
    });
}

function logout() {
    console.log("logout");
    amazon.Login.logout();
    resetUserInfo();
    refreshUi();
}

function refreshUi() {
    $('#login').text(userInfo.name);
    $('#access-token').val(userInfo.accessToken);
    if (userInfo.authenticated) {
        $('#logout').show();
    } else {
        $('#logout').hide();
    }
}

function loadUpcomingMenus() {
    console.log('loadUpcomingMenus');
    $.ajax({
        url : 'https://api.menu.witspirit.be/menus',
        method : 'GET',
        headers : {
            'Authorization' : userInfo.accessToken
        },
        dataType : 'json',
        jsonp : false,
        success : showMenus,
        error : reportAjaxError
    });
}

function showMenus(data, statusLine, xhr) {
    console.log('showMenus : '+JSON.stringify(data)+" ; "+statusLine);

    let display = "";
    data.forEach(menu => display += menu.date + ' : ' + menu.dinner + '\n');

    $('#menusResponse').val(display);
}

function reportAjaxError(xhr, statusLine, error) {
    console.log("reportAjaxError : "+statusLine+ " ; "+ JSON.stringify(error));
}


$(document).ready(function() {
    refreshUi();
    $('#login').click(login);
    $('#logout').click(logout);
    $('#loadMenus').click(function(event) {
        loadUpcomingMenus();
        event.preventDefault(); // Inhibit form submit
    });
});

console.log("End of menu-ui.js");