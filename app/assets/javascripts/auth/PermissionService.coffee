define ["zeezoo", "auth/AuthService"], ->
  angular.module("zeezoo").service 'PermissionService', (authService, $http) ->
    @userCanAddPlacemark = ->
      authService.isAuthenticated

    @userCanModify = (placemark, callback) ->
      $http.get("/placemarks/canModify/#{placemark?.id}").success (data) =>
        callback data