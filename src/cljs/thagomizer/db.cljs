(ns thagomizer.db)

(def default-db {:passcode "m00m00"
                 :authenticated nil
                 :uid nil
                 :text-field ""
                 :is-typing {:self false :others #{}}
                 :uids {}
                 :messages #queue []
                 :sms-status nil})