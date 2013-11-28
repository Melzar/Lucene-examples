<#import "/spring.ftl" as spring />
<!DOCTYPE html>
<html>
<head>
<script src="<@spring.url '/resources/angular/angular.js'/>"></script>
<script src="<@spring.url '/resources/angular/angular-ui-bootstrap.js'/>"></script>
<link type="text/css" href="<@spring.url '/resources/bootstrap/bootstrap.css'/>" rel="stylesheet">
</head>
<body ng-app="LuceneTest">


<script>

    var app = angular.module("LuceneTest", ['ui.bootstrap'])

    app.controller("TestController", function($scope, $http, limitToFilter){

        $scope.searchIndex = function(value)
        {
           $scope.responsestatus = "in progress...";
           return $http.get("<@spring.url '/rest/index/course/search'/>" + "?query=" + value).then(function(response){
                $scope.responsestatus = "success";
                $scope.responsedata = response.data;
                console.log(response);
                return limitToFilter(response.data.results, 10)
            })
        }

        $scope.create = function()
        {
            $scope.responsestatus = "in progress...";
            $http({
                url: "<@spring.url '/rest/index/course/createindex'/>",
                method: "POST",
                headers : {'Content-Type': 'application/json'}
            }).success(function(datares, status, headers, cfg){
                        $scope.responsestatus = "success";
            }).error(function(datares, status, headers, cfg){
                        $scope.responsestatus = "error";
            })
        }
    })

</script>
<div class="container">
    <div class="row">
            <div clas="col-md-12" ng-controller="TestController">
                    <div class="row">
                        <h1>Lucene example</h1>
                        <div class="col-md-6">
                            <pre>Model: {{lucene| json}}</pre>
                            <input type="text" class="form-control input-lg" typeahead-wait-ms="500" ng-model="lucene" typeahead="course as course.title for course in searchIndex($viewValue)" placeholder="enter query"  />
                        </div>
                        <div class="col-md-2">
                            <button class="btn btn-default" type="button" ng-click="create()">create index</button>
                        </div>
                        <div class="col-md-3" ng-if="responsedata">
                           Response status: {{responsestatus}} <br/>
                           Totalhits: {{responsedata.totalhits}} <br/>
                           Searchtime: {{responsedata.searchtime}} ms
                        </div>
                    </div>
            </div>
    </div>
</div>

</body>
</html>