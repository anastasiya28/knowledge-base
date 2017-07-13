function init(){
    var token = localStorage.getItem("token");
    if(token != null){
        showContent();
    } else {
        showLoginform();
    }
}

function login() {
    $.post("/login?login=" + $("#login").val() + "&password=" + $("#password").val())
    .done(function( data ) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("userrole", data.userrole);
        showContent();
        return;
    })
    .fail(function( data ) {unauthorized(data);});
    return;
}

function register() {
    $.post("/user/new?login=" + $("#login").val() + "&password=" + $("#password").val())
    .done(function( data ) {
        localStorage.setItem("token",data.token);
        localStorage.setItem("userrole", data.userrole);
        showContent();
        return;
    })
    .fail(function( data ) {unregistered(data);});
    return;
}

function logout() {
    localStorage.removeItem("token");
    localStorage.removeItem("userrole");
    showLoginform();
    return;
}

function unauthorized(data){
    var responseObject = JSON.parse(data.responseText);
    var str = '<div id="errorMessage">'+responseObject.errorMessage+'</div>';
    if($("#errorMessage").length > 0){
        $("#errorMessage").replaceWith(str);
    } else {
        showLoginform(str);
    }
    console.log(data);
}

function unregistered(data){
    var responseObject = JSON.parse(data.responseText);
    var str = '<div id="errorMessage">'+responseObject.errorMessage+'</div>';
    if($("#errorMessage").length > 0){
        $("#errorMessage").replaceWith(str);
    } else {
        showRegform(str);
    }
    console.log(data);
}

function handleError(data){
    var responseObject = JSON.parse(data.responseText);
    var str = '<div id="errorMessage">'+responseObject.errorMessage+'</div>';
    $("#errorMessage").replaceWith(str);
    console.log(data);
}


function showContent(){
    $.get("/html-templates/content.html")
    .done(function( data ) {
        $("#container").replaceWith(data);
        getSubsectionFromServer(1);
        getTagListFromServer();
        return;
    });
}

function showLoginform(errorMessage){
    $.get( "/html-templates/loginform.html")
    .done(function( data ) {
        $("#container").replaceWith(data);
        $("#loginButton").click(function() {
            if (errorMessage != null) {
               $("#errorMessage").replaceWith(errorMessage);
               }
            login();
        });
        $("#regesterButton").click(function() {
                showRegform(errorMessage);
        });
        return;
    });
}

function showRegform(errorMessage){
    $.get( "/html-templates/regform.html")
    .done(function( data ) {
        $("#container").replaceWith(data);
        $("#createAccount").click(function() {
           if (errorMessage != null) {
              $("#errorMessage").replaceWith(errorMessage);
           }
           register();
        });
        $("#cancelCreateAccount").click(function() {
            showLoginform(errorMessage);
        });
        return;
    });
}

function getSubsectionFromServer(id) {
   if(id !=1 & $("#parentId"+id).is(":visible")) {
      $("#parentId"+id).hide();
      getArticleListFromServer(id);
      return;
   }
   $.ajax({
       type: "GET",
       url: "/api/section/" + id + "/child_section_list",
       dataType: 'json',
       async: true,
       headers: { "Authorization": localStorage.getItem("token") },
       statusCode: {
             401:function( data ) {unauthorized(data);}
       },
       success: function( data ) {
           var str = '<ul id = "parentId'+ id +'" class="tree">';
           for (var i = 0; i < data.length; i++) {
              var sectionId = data[i].id;
              var sectionName = data[i].name;
              str += '<li id = "sectionId'+ sectionId +'" class="branch" onclick="getSubsectionFromServer('+sectionId+'); event.stopPropagation();">' +
                '<div class="content-name">';
              if(localStorage.getItem("userrole") == "ROLE_ADMIN") {
                  str += '<i class="fa fa-pencil-square-o" aria-hidden="true" onclick="showSectionEditor('+sectionId+'); event.stopPropagation();"></i>&nbsp;' +
                  '<i class="fa fa-plus-square-o" aria-hidden="true"></i>&nbsp;' +
                  '<i class="fa fa-file-text-o" aria-hidden="true" onclick="showArticleFormForAdding('+sectionId+'); event.stopPropagation();"></i>&nbsp;' +
                  '<i class="fa fa-trash-o" aria-hidden="true"></i>&nbsp;';
              }
              str += sectionName + '</div>' +
//                '<div class="content-name">' + sectionName + '</div>' +
                '<div id = "parentId'+ sectionId +'" style="display: none;"></div>' +
              '</li>';
           }
           str += '</ul>';
           $("#parentId" + id).replaceWith(str);
           getArticleListFromServer(id);
           return;
           }
        });
   return;
}


function getArticleListFromServer(sectionId){
    $.ajax({
        type: "GET",
        url: "/api/section/" + sectionId + "/article_list",
        dataType: 'json',
        async: true,
        headers: { "Authorization": localStorage.getItem("token") },
        statusCode: {
            401:function( data ) {unauthorized(data);}
        },
        success: function( data ) {
            if(data.length == 1){
                showArticle(data[0].id, sectionId);
                return;
            }
            var str = '<div id="articleList"><ul id = "sectionId'+ sectionId +'" class="tree">';
            for (var i = 0; i < data.length; i++) {
                var articleId = data[i].id;
                var articleName = data[i].name;
                str += '<li id = "articleId'+ articleId +'" class="branch content-name" onclick="showArticle('+articleId+', '+sectionId+');">' + articleName + '</li>';
            }
            str += '</ul></div>';
            $("#articleList").replaceWith(str);
            return;
        }
    })
}

function showArticle(articleId, sectionId){
    var handleArticle = function(article) {
        var str = '<div id="articleList">';
        str += '<i class="fa fa-arrow-left articleButton" aria-hidden="true" onclick="getArticleListFromServer('+ sectionId +');"></i>&nbsp;';
        if(localStorage.getItem("userrole") == "ROLE_ADMIN") {
//          str += '<i class="fa fa-arrow-left articleButton" aria-hidden="true" onclick="getArticleListFromServer('+ sectionId +');"></i>&nbsp;';
          str += '<i class="fa fa-pencil-square-o articleButton" aria-hidden="true" onclick="showArticleEditor('+articleId+');"></i>&nbsp;';
          str += '<i class="fa fa-trash-o articleButton" aria-hidden="true" onclick="showArticleFormForDeleting('+articleId+', '+sectionId+');"></i>&nbsp;';
        }
        str += markdown.toHTML(article.content) + '<br><div id = "articleTags"></div></div>';
        $("#articleList").replaceWith(str);
        getArticleTags(articleId);
        return;
    };
    getArticle(articleId, handleArticle);
}

function getArticle(id, callBackFunction) {
    $.ajax({
       type: "GET",
       url: "/api/article/" + id,
       dataType: 'json',
       async: true,
       headers: { "Authorization": localStorage.getItem("token") },
       statusCode: {
            401:function(data) {unauthorized(data);}
       },
       success: function(data) {
            callBackFunction(data);
       },
        error: function(data){
             console.log(data);
        }
    });
}

function getArticleTags(article_id) {
   $.ajax({
       type: "GET",
       url: "/api/tag/article/" + article_id,
       dataType: 'json',
       async: true,
       headers: { "Authorization": localStorage.getItem("token") },
       statusCode: {
             401:function( data ) {unauthorized(data);}
       },
       success: function( data ) {
           var str = '<div id = "articleTags">';
           for (var i = 0; i < data.length; i++) {
              var tagId = data[i].id;
              var tagName = data[i].name;
              str += '<a style="cursor: pointer;" onclick="getArticleByTag('+tagId+');">'+tagName+',</a>&nbsp; ';
           }
           str += '</div>';
           $("#articleTags").replaceWith(str);
//           getArticleListFromServer(id);
           return;
           }
        });
   return;
}


function getTagListFromServer(){
    $.ajax({
        type: "GET",
        url: "/api/tag/list",
        dataType: 'json',
        async: true,
        headers: { "Authorization": localStorage.getItem("token") },
        statusCode: {
            401:function( data ) {unauthorized(data);}
        },
        success: function( data ) {
            var str = '<div id="tagList"><ul>';
            for (var i = 0; i < data.length; i++) {
                var tagId = data[i].id;
                var tagName = data[i].name;
                data[i].text = data[i].name;
                data[i].weight = randomInteger(1, 10);
                data[i].html = {"onclick":"getArticleByTag("+tagId+");"};

                str += '<li onclick="getArticleByTag('+tagId+');">'+tagName+'</li>';
            }
            str += '</ul></div>';
            $(function () {
                $("#tagList").jQCloud(data);
            });
            return;
        }
    });
    return;
}


function getArticleByTag(tagId) {
    $.ajax({
        type: "GET",
        url: "/api/article/tag/" + tagId,
        dataType: 'json',
        async: true,
        headers: { "Authorization": localStorage.getItem("token") },
        statusCode: {
            401:function( data ) {unauthorized(data);}
        },
        success: function( data ) {
            if(data.length == 1){
                showArticle(data[0].id);
                return;
            }
            var str = '<div id="articleList"><ul id = "tagId'+ tagId +'" class="tree">';
            for (var i = 0; i < data.length; i++) {
                var articleId = data[i].id;
                var articleName = data[i].name;
                str += '<li id = "articleId'+ articleId +'" class="branch content-name" onclick="showArticle('+articleId+');">' + articleName + '</li>';
            }
            str += '</ul></div>';
            $("#articleList").replaceWith(str);
            return;
        }
    });
    return;
}

function randomInteger(min, max) {
    var rand = min - 0.5 + Math.random() * (max - min + 1)
    rand = Math.round(rand);
    return rand;
}

//--------------- Административные функции ---------------------------
function showSectionEditor(sectionId){
    $.get( "/html-templates/sectionEditor.html")
    .done(function(data) {
        $("#articleList").replaceWith(data);
        var handleSection = function(section) {
           $("#parentSectionName").val(section.parentId);
           $("#sectionName").val(section.name);
           var handleParentSection = function(parentSection) {
               $("#parentSectionName").val(parentSection.name);
           }
           $("#saveSectionButton").click(function() {
               updateSection(section.id, section.parentId);
           });
           $("#cancelSaveSectionButton").click(function() {
                getSubsectionFromServer(sectionId);
           });
           getSection(section.parentId, handleParentSection);
        };
        getSection(sectionId, handleSection);
        return;
    });
}

function getSection(sectionId, callBackFunction){
    $.ajax({
       type: "GET",
       url: "/api/section/" + sectionId,
       dataType: 'json',
       async: true,
       headers: { "Authorization": localStorage.getItem("token") },
       statusCode: {
            401:function( data ) {unauthorized(data);}
       },
       success: function( data ) {
            callBackFunction(data);
       }
    });
}

function updateSection(id, parentId){
    $.ajax({
        type: "POST",
        url: "/api/section/" + id + "/update?id=" + id + "&name=" + $("#sectionName").val(),
        dataType: 'json',
        async: true,
        headers: { "Authorization": localStorage.getItem("token") },
        statusCode: {
            401:function(data) {unauthorized(data);}
        },
        success: function(data) {
            var str = '<div id="errorMessage"> Данные раздела успешно обновлены </div>';
            $("#errorMessage").replaceWith(str);
            $("#errorMessage").effect("highlight", {color: 'blue'}, 1500);
            setTimeout(getSubsectionFromServer, 1500, parentId);
            setTimeout(getSubsectionFromServer, 1500, parentId);
        },
        error: function(data){
           handleError(data);
        }
    });
}

function showArticleEditor(id){
    $.get( "/html-templates/articleEditor.html")
    .done(function( data ) {
        $("#articleList").replaceWith(data); //отображаем форму редактирования статьи. Пока пустую. Заполняем ее ниже.
        var handleArticle = function(article) {
            str = '<div id="articleEdit" class="col-md-12 article-editor"><textarea id="articleSource">' + article.content + '</textarea></div>';
            $("#articleEdit").replaceWith(str);
            $("#updateArticleButton").click(function() {
                updateArticle(article.id);
            });
            $("#cancelSaveArticleButton").click(function() {
                showArticle(article.id);
            });
            return;
        };
        getArticle(id, handleArticle);
        return;
    });
}

function updateArticle(id){
    var content = $("#articleSource").val();
    $.ajax({
        type: "POST",
        url: "/api/article/" + id + "/update",
        data: "id=" + id + "&content=" + content,
        dataType: 'json',
        async: true,
        headers: { "Authorization": localStorage.getItem("token") },
        statusCode: {
            401:function(data) {unauthorized(data);}
        },
        success: function(data) {
            var str = '<div id="errorMessage"> Статья успешно изменена.</div>';
            $("#errorMessage").replaceWith(str);
            $("#errorMessage").effect("highlight", {color: 'blue'}, 1500);
            setTimeout(showArticle, 1500, id);
        },
        error: function(data){
           handleError(data);
        }
    });
}

function showArticleFormForAdding(sectionId){
    $.get( "/html-templates/articleFormForAdding.html")
    .done(function(data) {
        $("#articleList").replaceWith(data);
        var handleSection = function(section) {
           $("#sectionId").val(section.id);
           $("#sectionName").val(section.name);
               $("#saveArticleButton").click(function() {
                    createArticle();
               });
                $("#cancelSaveArticleButton").click(function() {
                    getArticleListFromServer(sectionId);
                });
        };
        getSection(sectionId, handleSection);
        return;
    });
}

function createArticle(){
      var sectionId = $("#sectionId").val();
      var articleTitle = $("#articleTitle").val();
      var articleFaleName = $("#articleFaleName").val();
      var articleContent = $("#articleContent").val();
    $.ajax({
        type: "POST",
        url: "/api/article/new",
        data: "&sectionId=" + sectionId + "&articleTitle=" + articleTitle + "&articleFaleName=" + articleFaleName + "&articleContent=" + articleContent,
        dataType: 'json',
        async: true,
        headers: { "Authorization": localStorage.getItem("token") },
        statusCode: {
            401:function(data) {unauthorized(data);}
        },
        success: function(data) {
            var str = '<div id="errorMessage"> Статья успешно добавлена.</div>';
            $("#errorMessage").replaceWith(str);
            $("#errorMessage").effect("highlight", {color: 'blue'}, 1500);
              setTimeout(getArticleListFromServer, 1500, sectionId);
        },
        error: function(data){
           handleError(data);
        }
    });
}

function showArticleFormForDeleting(articleId, sectionId){
    $.get("/html-templates/articleFormForDeleting.html")
    .done(function(data) {
        $("#articleList").replaceWith(data);
        var handleArticle = function(article) {
//           $("#sectionId").val(article.sectionId);
           $("#articleTitle").val(article.title);
           var handleSection = function(section) {
              $("#sectionId").val(section.id);
              $("#sectionName").val(section.name);
           }
                 $("#deleteArticleButton").click(function() {
                    deleteArticle(articleId, sectionId);
                 });
                 $("#cancelDeleteArticleButton").click(function() {
                    showArticle(articleId, sectionId);;
                  });
           getSection(sectionId, handleSection);
        };
        getArticle(articleId, handleArticle);
        return;
    });
}

function deleteArticle(articleId, sectionId){
    $.ajax({
        type: "POST",
        url: "/api/article/" + articleId + "/delete",
        data: "id=" + articleId,
        dataType: 'json',
        async: true,
        headers: { "Authorization": localStorage.getItem("token") },
        statusCode: {
            401:function(data) {unauthorized(data);}
        },
        success: function(data) {
            var str = '<div id="errorMessage"> Статья успешно удалена.</div>';
            $("#errorMessage").replaceWith(str);
            $("#errorMessage").effect("highlight", {color: 'blue'}, 1500);
              setTimeout(getArticleListFromServer, 1500, sectionId);
        },
        error: function(data){
           handleError(data);
        }
    });
}