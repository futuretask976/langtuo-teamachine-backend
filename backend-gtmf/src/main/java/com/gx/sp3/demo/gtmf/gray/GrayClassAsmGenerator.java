package com.gx.sp3.demo.gtmf.gray;//package com.gx.gtmf.framework.gray;
//
//import org.springframework.asm.*;
//
///**
// * @author miya
// */
//// TODO <<clinit>>未按预期生成静态变量的初始化
//public class GrayClassAsmGenerator extends ClassVisitor {
//    private final String bizCode;
//    private final String className;
//
//    class NameSpaceAnnotationTransform extends AnnotationVisitor {
//        public NameSpaceAnnotationTransform(int api, AnnotationVisitor av) {
//            super(api, av);
//        }
//
//        @Override
//        public void visit(String name, Object value) {
//            if ( name.equals("desc")) {
//                value = ((String)value).replace("${bizCode}", bizCode);
//            } else if ( name.equals("nameSpace")) {
//                value = ((String)value).replace("${bizCode}", bizCode);
//            }
//
//            super.visit(name, value);
//        }
//    }
//
//    class AppSwitchAnnotationTransform extends AnnotationVisitor {
//
//        public AppSwitchAnnotationTransform(int api, AnnotationVisitor av) {
//            super(api, av);
//        }
//
//        @Override
//        public void visit(String name, Object value) {
//            if ( name.equals("des")) {
//                value = ((String)value).replace("${bizCode}", bizCode);
//            }
//            super.visit(name, value);
//        }
//    }
//
//    class SwitchFieldTransform extends FieldVisitor {
//
//        public SwitchFieldTransform(int api, FieldVisitor fv) {
//            super(api, fv);
//        }
//
//        @Override
//        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//            AnnotationVisitor av = super.visitAnnotation(desc, visible);
//            if ( desc.equals(Type.getDescriptor(AppSwitch.class))) {
//                return new AppSwitchAnnotationTransform(this.api, av);
//            }
//
//            return av;
//        }
//    }
//
//
//    public GrayClassAsmGenerator(int api, ClassWriter cw, String bizCode, String className) {
//        super(api, cw);
//        this.bizCode =bizCode;
//        this.className = className;
//    }
//
//    @Override
//    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//        //set class name
//        name = className.replace(".","/");
//        super.visit(version, access, name, signature, superName, interfaces);
//    }
//
//    @Override
//    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//        //namespace 注解
//        AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
//        if ( desc.equals(Type.getDescriptor(NameSpace.class))) {
//            return new NameSpaceAnnotationTransform(this.api, annotationVisitor);
//        }
//
//        return annotationVisitor;
//    }
//
//    @Override
//    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
//        return new SwitchFieldTransform(this.api,super.visitField(access, name, desc, signature, value));
//    }
//}
