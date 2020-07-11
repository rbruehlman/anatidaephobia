(ns thagomizer.db)

(def default-db {:uid nil
                 :text-field ""
                 :is-typing {:self false :others #{}}
                 :uids {}
                 :messages #queue [{:msg "Hello!" :timestamp 1594343419000 :uid "1234"}
                                   {:msg "After his research into TikTok’s clipboard was published in March, Mr. Mysk and a colleague took another look at TikTok and discovered that it was sending videos without using a standard internet encryption protocol—a design decision that could give hackers a way of spoofing TikTok videos from legitimate users. TikTok has since fixed this issue, Mr. Mysk said, but according to him, it was another sign that the product’s security was substandard." :timestamp 1594343419000 :uid "456"}
                                   {:msg "The now-retracted email was sent as an alert to thousands of Amazon employees early in the business day in Seattle: “Due to security risks, the TikTok app is no longer permitted on mobile devices that access Amazon email. If you have TikTok on your device, you must remove it by 10-Jul to retain mobile access to Amazon email. At this time, using TikTok from your Amazon laptop browser is allowed.”" :timestamp 1594343419000 :uid "456"}
                                   {:msg "The first message was dramatic enough, as the email directive to employees appeared to buttress recent scrutiny of TikTok security issues from governments in the U.S. and India." :timestamp 1594343419000 :uid "456"}
                                   {:msg "Robert McMillan and Dana Mattioli contributed to this article." :timestamp 1594343419000 :uid "456"}]})