NOT_FOUND = -1

findPlacemarkIndex = (placemark, array) ->
  results = (index for index in [0...array.length] when placemark.equalsTo array[index])
  if results.length > 0
    results[0]
  else
    NOT_FOUND

createListener = (method) ->
  (event, parameters...) ->
    method parameters...

define [
  "zeezoo",
  "auth/PermissionService",
  "behaviours/PlacemarkBehaviour",
  "behaviours/TemporaryPlacemarkBehaviour",
  "map/behaviours"
], ->
  angular.module('zeezoo').controller 'ApplicationController', ($scope, Placemark, PermissionService, PlacemarkBehaviour, TemporaryPlacemarkBehaviour, BasicBehaviour) ->
    $scope.displayedPlacemarks = []
    $scope.detailedMode = false

    $scope.initMap = (mapContainer) ->
      mapContainer.add PlacemarkBehaviour, 'displayedPlacemarks'
      mapContainer.add TemporaryPlacemarkBehaviour
      mapContainer.attach PlacemarkBehaviour
      $scope.mapContainer = mapContainer
      $scope.initializeListeners()

    $scope.modes =
      VIEWER: 'vewer mode'
      EDITOR: 'editor mode'
      CREATOR: 'creator mode'

    $scope.mode = $scope.modes.VIEWER

    $scope.changeMode = (mode) ->
      if mode not in (value for property, value of $scope.modes)
        throw new Error "Unknown mode '#{mode}'"
      $scope.mode = mode
      if mode == $scope.modes.CREATOR
        $scope.mapContainer.attach TemporaryPlacemarkBehaviour
      else
        $scope.mapContainer.detach TemporaryPlacemarkBehaviour

    updateBorder = (border)->
      southEast = border.getSouthEast()
      northWest = border.getNorthWest()

      boundaries =
        south: southEast.lat
        north: northWest.lat
        west: northWest.lng
        east: southEast.lng

      placemarks = Placemark.query boundaries, ->
        $scope.displayedPlacemarks = placemarks

    $scope.activateDetailedMode = (placemark) ->
      $scope.detailedMode = true
      $scope.activePlacemark = angular.copy placemark

    $scope.deactivateDetailedMode = ->
      $scope.detailedMode = false
      $scope.activePlacemark = null

    $scope.addPlacemark = (placemark) ->
      $scope.displayedPlacemarks.push placemark

    $scope.replacePlacemark = (placemark) ->
      index = findPlacemarkIndex(placemark, $scope.displayedPlacemarks)
      $scope.displayedPlacemarks.splice(index, 1, placemark)

    $scope.removePlacemark = (placemark) ->
      index = findPlacemarkIndex(placemark, $scope.displayedPlacemarks)
      $scope.displayedPlacemarks.splice index, 1

    $scope.deactivateCurrentPlacemark = ->
      $scope.mapContainer.emit PlacemarkBehaviour.PLACEMARK_DEACTIVATION_REQUESTED

    $scope.prepareForPlacemarkCreation = ->
      $scope.deactivateCurrentPlacemark()
      $scope.activateDetailedMode()
      $scope.changeMode($scope.modes.CREATOR)

    $scope.userCanAddPlacemark = ->
      PermissionService.userCanAddPlacemark()

    createPlacemark = (location) ->
      if $scope.mode == $scope.modes.CREATOR
        placemark = new Placemark {lat: location.lat, lon: location.lng}
        $scope.mapContainer.emit TemporaryPlacemarkBehaviour.TEMPORARY_PLACEMARK_CREATED, location
        $scope.activateDetailedMode placemark

    $scope.initializeListeners = ->
      $scope.mapContainer.on BasicBehaviour.BOUNDS_CHANGED, createListener updateBorder
      $scope.mapContainer.on BasicBehaviour.MAP_CLICKED, createListener createPlacemark
      $scope.mapContainer.on PlacemarkBehaviour.PLACEMARK_ACTIVATED, createListener $scope.activateDetailedMode
      $scope.mapContainer.on PlacemarkBehaviour.PLACEMARK_DEACTIVATED, createListener $scope.deactivateDetailedMode

  console.log "app controller loaded"