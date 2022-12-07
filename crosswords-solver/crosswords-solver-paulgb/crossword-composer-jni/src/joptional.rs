use jni::JNIEnv;
use jni::objects::JValue;

pub struct JOptional<'a> {
    env: JNIEnv<'a>
}

impl<'a> JOptional<'a> {
    pub fn new(jni_env: JNIEnv<'a>) -> Self {
        Self {
            env: jni_env
        }
    }

    pub fn empty(self: Self) -> JValue<'a> {
        self.call("empty", "()Ljava/util/Optional;", &[])
    }

    pub fn of(self: Self, value: JValue) -> JValue<'a> {
        self.call("of", "(Ljava/lang/Object;)Ljava/util/Optional;", &[value])
    }

    fn call(self: Self, method: &str, signature: &str, args: &[JValue]) -> JValue<'a> {
        let optional_class = self.env.find_class("java/util/Optional").unwrap();
        self.env.call_static_method(optional_class, method, signature,
                                    args).unwrap()
    }
}