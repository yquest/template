package pt.fabm.template

/**
 * Event bus addresses
 */
object EventBusAddresses {
  object Dao {
    object User {
      /**
       * in: [pt.fabm.template.models.UserRegisterIn]
       *
       * out: none
       */
      const val create: String = "dao.user.create"

      /**
       * in: [pt.fabm.template.models.Login]
       *
       * out: [Boolean]
       */
      const val login: String = "dao.user.login"
    }

    object Car {
      /**
       * in: none
       *
       * out: List<[pt.fabm.template.models.Car]>
       */
      const val list: String = "dao.car.list"
      /**
       * in: [pt.fabm.template.models.Car]
       *
       * out: none
       */
      const val create: String = "dao.car.create"
      /**
       * in: [pt.fabm.template.models.CarId]
       *
       * out: none
       */
      const val delete: String = "dao.car.delete"
      /**
       * in: [pt.fabm.template.models.Car]
       *
       * out: none
       */
      const val update: String = "dao.car.update"
      /**
       * in: [pt.fabm.template.models.CarId]
       *
       * out: none
       */
      const val retrieve: String = "dao.car.retrieve"
    }
  }
}
