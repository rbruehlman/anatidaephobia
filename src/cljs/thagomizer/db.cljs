(ns thagomizer.db)

(def default-db {:passcode nil
                 :admin nil
                 :chat {:authenticated nil
                        :uid nil
                        :text-field ""
                        :is-typing {:self false :others #{}}
                        :uids {}
                        :messages #queue []
                        :camera {:stream {:visible false :loading false}
                                 :photo {:visible false :loading false}
                                 :modal false}}
                 :send {:authenticated nil
                        :text-field ""}
                 :receipt  {:authenticated nil
                            :messages {:data []
                                       :error nil}}})