require [
  "map/directives",
  "map/utils"
], ->
  m = angular.module 'zeezoo', ['infra-map', 'ngResource', 'ui.bootstrap', 'ui']

  m.factory 'Placemark', ($resource) ->
    urlTemplate = '/placemark/:PlacemarkId'

    Placemark = $resource urlTemplate, {PlacemarkId: '@id'}

    Placemark::equalsTo = (placemark) ->
      @id == placemark.id

    Placemark

  m.factory 'Business', ($resource) ->
    urlTemplate = '/business/:BusinessId'

    $resource urlTemplate, {BusinessId: '@id'}


  m.controller 'EditorController', ($scope) ->
    $scope.updateActivePlacemark = ->
      $scope.activePlacemark.$update ->
        $scope.replacePlacemark $scope.activePlacemark
        $scope.changeMode $scope.modes.VIEWER

  m.controller 'CreatorController', ($scope) ->
    $scope.saveNewPlacemark = ->
      $scope.activePlacemark.$save ->
        $scope.addPlacemark $scope.activePlacemark
        $scope.changeMode $scope.modes.VIEWER

  require [
    "controllers/ApplicationController",
    "controllers/BusinessController",
    "controllers/DetailsController",
    "auth/AuthController"
  ], ->
    console.log "launching..."
    angular.bootstrap(document, ['zeezoo']);
    console.log "angular.bootstrap"