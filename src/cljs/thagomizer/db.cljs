(ns thagomizer.db)

(def default-db {:passcode nil
                 :authenticated nil
                 :uid nil
                 :text-field ""
                 :is-typing {:self false :others #{}}
                 :uids {}
                 :messages #queue []})