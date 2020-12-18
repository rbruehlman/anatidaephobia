(ns thagomizer.db)

(def default-db {:passcode nil
                 :chat {:authenticated nil
                        :history-retention false
                        :uid nil
                        :text-field ""
                        :is-typing {:self false :others #{}}
                        :uids {}
                        :messages #queue []
                        :camera {:stream {:visible false :loading false}
                                 :photo {:visible false :loading false}
                                 :modal false}}
                 :send {:authenticated nil
                        :message nil
                        :text-field ""}
                 :receipt  {:authenticated nil
                            :messages {:data []
                                       :error nil}}})