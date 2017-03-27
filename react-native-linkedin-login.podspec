Pod::Spec.new do |s|

  s.name         = "react-native-linkedin-login"
  s.version      = "1.3.0"
  s.summary      = "iOS react-native LinkedIn Login support"

  s.homepage     = "https://github.com/jodybrewster/react-native-linkedin-login.git"
  s.social_media_url = 'https://twitter.com/jodybrewster'
  s.license      = { :type => "MIT", :file => "LICENSE" }
  s.author       = { 'jodybrewster' => '@jodybrewster' }

  s.platform     = :ios, '7.0'

  s.source = { :git => "https://github.com/techsophy-inc/react-native-linkedin-login.git", :tag => "#{s.version}" }
  s.source_files        = 'RNLinkedinLogin/*'
  s.preserve_paths      = 'linkedin-sdk.framework'
  s.vendored_frameworks = 'linkedin-sdk.framework'
  s.requires_arc = true

  s.dependency 'AFNetworking', '>= 2.6.3'
  s.dependency 'IOSLinkedInAPIFix', '>= 2.0.3'

end