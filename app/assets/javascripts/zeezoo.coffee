require [
  "map/directives",
  "map/utils"
], ->
  m = angular.module 'zeezoo', ['infra-map', 'ngResource', 'ui.bootstrap', 'ui']

  m.factory 'Placemark', ($resource) ->
    urlTemplate = '/api/placemark/:id'

    Placemark = $resource urlTemplate, {id: '@id'}, {
      find: {method: "POST", isArray: true}
    }

    Placemark::equalsTo = (placemark) ->
      @id == placemark.id

    Placemark

  m.factory 'Business', ($resource) ->
    urlTemplate = '/api/business/:BusinessId'

    $resource urlTemplate, {BusinessId: '@id'}


  m.controller 'EditorController', ($scope) ->
    $scope.updateActivePlacemark = ->
      $scope.activePlacemark.$update ->
        $scope.replacePlacemark $scope.activePlacemark
        $scope.changeMode $scope.modes.VIEWER

  m.controller 'MenuController', ($scope, $http) ->
    $scope.menu = []
    $http.get("/api/vertical").success (list)->
      $scope.menu = list

    $scope.activeClass = (v) -> if v is $scope.currentVertical then "active" else ""

  require [
    "controllers/ApplicationController",
    "controllers/BusinessController",
    "controllers/DetailsController",
    "auth/AuthController",
    "MapService"
  ], ->

    m.controller 'CreatorController', ($scope, Placemark, mapService) ->
      $scope.saveNewPlacemark = ->
        mapService.pushPlacemark $scope.activePlacemark, (p)->
            $scope.addPlacemark p
            $scope.changeMode $scope.modes.VIEWER

    angular.bootstrap(document, ['zeezoo']);