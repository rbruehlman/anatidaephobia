(ns thagomizer.db)

(def default-db {:uid nil
                 :text-field ""
                 :is-typing {:self false :others #{}}
                 :messages #queue []})