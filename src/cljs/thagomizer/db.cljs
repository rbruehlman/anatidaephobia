(ns thagomizer.db)

(def default-db {:passcode nil
                 :admin nil
                 :chat {:authenticated nil
                        :uid nil
                        :text-field ""
                        :hidden false
                        :is-typing {:self false
                                    :others #{}}
                        :uids {}
                        :messages #queue []
                        :camera {:stream {:element nil
                                          :object nil
                                          :visible false
                                          :error nil
                                          :loading nil}
                                 :photo {:url nil
                                         :error nil
                                         :loading nil}
                                 :modal false}}
                 :send {:authenticated nil
                        :text-field ""
                        :visible true}
                 :receipt  {:authenticated nil
                            :messages {:data []
                                       :error nil}}})