<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html <%--lang="en"--%>>

<head>
  <title>Basic site for SO Question #42927030</title>
  <meta charset="utf-8" />

  <meta http-equiv="X-UA-Compatible" content="IE=edge" />
  <meta name="viewport" content="width=device-width, initial-scale=1, minimal-ui" />

  <meta name="robots" content="index,follow" />

  <meta name="description" content="" />
  <meta name="keywords" content="" />
  <meta name="author" content="Octavian Theodor Nita (http://github.com/octavian-nita)" />

  <link rel="stylesheet" href="<c:url value="static/css/main.css" />" />
</head>

<body>

<main class="main"><span>Here's <em>${sessionScope.johnny}</em>!</span></main>

<!-- Load scripts at body's end so as not to block rendering. -->
<script src="<c:url value="static/js/main.js" />"></script>
</body>

</html>
