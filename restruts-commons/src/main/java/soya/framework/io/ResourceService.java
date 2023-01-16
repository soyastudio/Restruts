package soya.framework.io;

import soya.framework.convert.ConvertService;
import soya.framework.pattern.Named;
import soya.framework.reflect.ReflectUtils;
import soya.framework.util.StreamUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public abstract class ResourceService {

    private static ResourceService INSTANCE;
    protected Map<String, Resource> resourceMap = new HashMap<>();
    protected Map<String, Class<? extends ResourceFilter>> fragmentProcessors = new HashMap<>();

    protected ResourceService() {
        INSTANCE = this;
    }

    private static ResourceService getInstance() {
        if (INSTANCE == null) {
            new DefaultResourceService();
        }
        return INSTANCE;
    }

    public static String[] supportedSchemas() {
        return getInstance().resourceMap.keySet().toArray(new String[getInstance().resourceMap.size()]);
    }

    public static String[] supportedFragmentProcessors() {
        return getInstance().fragmentProcessors.keySet().toArray(new String[getInstance().fragmentProcessors.size()]);
    }

    public static InputStream getAsInputStream(URI uri) throws ResourceException {
        if (getInstance().resourceMap.containsKey(uri.getScheme())) {
            return getInstance().resourceMap.get(uri.getScheme()).getAsInputStream(uri);

        } else {
            try {
                return uri.toURL().openStream();

            } catch (IOException e) {
                throw new ResourceException(e);
            }
        }
    }

    public static String getAsString(URI uri) throws ResourceException {
        return getAsString(uri, Charset.defaultCharset());
    }

    public static String getAsString(URI uri, Charset charset) throws ResourceException {
        try {
            return new String(StreamUtils.copyToByteArray(getAsInputStream(uri)), charset);

        } catch (IOException e) {
            throw new ResourceException(e);
        }
    }

    public static class DefaultResourceService extends ResourceService {
        private static final String DEFAULT_PACKAGE = ResourceService.class.getPackage().getName();
        public static final String[] DEFAULT_PACKAGES = new String[]{DEFAULT_PACKAGE, DEFAULT_PACKAGE + ".resources", DEFAULT_PACKAGE + ".fragments"};

        protected DefaultResourceService() {
            this(DEFAULT_PACKAGES);
        }

        public DefaultResourceService(String[] packages) {
            super();
            Objects.requireNonNull(packages);
            Set<Class<?>> set = new HashSet<>();
            Arrays.stream(packages).forEach(e -> {
                set.addAll(ReflectUtils.findClassesOfPackage(e));
            });

            register(set.toArray(new Class<?>[set.size()]));

        }

        public DefaultResourceService(Class<?>[] classes) {
            super();
            register(classes);
        }

        protected void register(Class<?>[] classes) {
            Objects.requireNonNull(classes);
            for (Class<?> rt : classes) {
                Named named = rt.getAnnotation(Named.class);
                if (named != null && !rt.isInterface() && !Modifier.isAbstract(rt.getModifiers())) {
                    String name = named.value();
                    if (Resource.class.isAssignableFrom(rt)) {
                        try {
                            if (!resourceMap.containsKey(name)) {
                                resourceMap.put(name, (Resource) rt.newInstance());

                            } else if (!resourceMap.get(name).getClass().equals(rt)) {
                                throw new IllegalArgumentException("Resource '" + name + "' already exists.");
                            }
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new ResourceException(e);
                        }

                    } else if ((ResourceFilter.class.isAssignableFrom(rt))) {
                        if (!fragmentProcessors.containsKey(name)) {
                            fragmentProcessors.put(name, (Class<? extends ResourceFilter>) rt);

                        } else if (!fragmentProcessors.get(name).equals(rt)) {
                            throw new IllegalArgumentException("Fragment processor '" + name + "' already exists.");

                        }
                    }
                }
            }
        }
    }

    public static class FragmentProcessChain {

        private List<ResourceFilter> processors = new ArrayList<>();

        public FragmentProcessChain(String fragment) {
            if (fragment != null) {
                StringTokenizer tokenizer = new StringTokenizer(fragment, ")");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    if (token.startsWith(".")) {
                        token = token.substring(1);
                    }

                    int separator = token.indexOf('(');
                    if (separator > 0) {
                        String name = token.substring(0, separator);
                        String params = token.substring(separator + 1);

                        Class<? extends ResourceFilter> type = getInstance().fragmentProcessors.get(name);
                        if (type == null) {
                            throw new IllegalArgumentException("Cannot find resource processor named as: " + name);
                        }

                        processors.add(create(type, params));

                    }
                }
            }
        }

        private ResourceFilter create(Class<? extends ResourceFilter> type, String param) {

            Constructor constructor = type.getConstructors()[0];
            Parameter[] parameters = constructor.getParameters();
            Object[] args = compile(param, parameters);

            try {
                return (ResourceFilter) constructor.newInstance(args);

            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                throw new IllegalArgumentException(e);
            }
        }

        private Object[] compile(String string, Parameter[] parameters) {
            Object[] args = new Object[parameters.length];
            if (args.length == 0 && string.length() != 0) {
                throw new IllegalArgumentException("Processor constructor error: arguments not match.");

            } else if (args.length == 1) {
                Class<?> type = parameters[0].getType();
                if (!type.isArray()) {
                    args[0] = ConvertService.convert(string, parameters[0].getType());

                } else {
                    String token = string;
                    if (token.startsWith("[") && token.endsWith("]")) {
                        token = token.substring(1, token.length() - 1);
                    }
                    String[] arr = token.split(",");

                    Object o = Array.newInstance(type.getComponentType(), arr.length);
                    for (int i = 0; i < arr.length; i++) {
                        Array.set(o, i, ConvertService.convert(arr[i], type.getComponentType()));
                    }
                    args[0] = o;
                }

            } else {
                // FIXME:
                String token = string;
                List<String> paramValues = new ArrayList<>();
                if (!token.contains("[")) {
                    Arrays.stream(token.split(",")).forEach(e -> {
                        paramValues.add(e);
                    });
                } else {

                    // FIXME:
                    int index = token.indexOf('[');
                    while (index >= 0) {
                        int end = token.indexOf(']');

                        String left = token.substring(0, index);
                        if (left.endsWith(",")) {
                            left = left.substring(0, left.length() - 1);
                        }

                        if (!left.isEmpty()) {
                            Arrays.stream(left.split(",")).forEach(e -> {
                                paramValues.add(e);
                            });
                        }

                        String middle = token.substring(index, end + 1);
                        paramValues.add(middle);

                        token = token.substring(end + 1);
                        if (token.startsWith(",")) {
                            token = token.substring(1);
                        }

                        index = token.indexOf('[');
                    }
                }

                if(!token.isEmpty()){
                    Arrays.stream(token.split(",")).forEach(e -> {
                        paramValues.add(e);
                    });
                }

                paramValues.forEach(e -> {
                    System.out.println("================ " + e);
                });


                for (int i = 0; i < parameters.length; i++) {
                    Class<?> type = parameters[i].getType();
                    if (!type.isArray()) {
                        if (token.startsWith("[")) {
                            throw new IllegalArgumentException("Arguments parsing error: not expecting an array.");
                        } else {
                            int sep = token.indexOf(',');
                            String value = token.substring(0, sep);
                            token = token.substring(sep + 1);
                            args[i] = ConvertService.convert(value, type);
                        }
                    } else {
                        if (!token.startsWith("[")) {
                            throw new IllegalArgumentException("Arguments parsing error: expecting an array.");

                        } else {
                            int sep = token.indexOf(']');
                            String value = token.substring(1, sep);
                            token = token.substring(sep + 1);
                            if (token.startsWith(",")) {
                                token = token.substring(1);
                            }
                            String[] arr = value.split(",");
                            Object o = Array.newInstance(type.getComponentType(), arr.length);
                            for (int j = 0; j < arr.length; j++) {
                                Array.set(o, j, ConvertService.convert(arr[j], type.getComponentType()));
                            }
                            args[i] = o;
                        }
                    }
                }
            }

            return args;
        }

        public InputStream process(InputStream in) throws IOException {
            if (processors.isEmpty()) {
                return in;
            }

            byte[] data = StreamUtils.copyToByteArray(in);
            Queue<ResourceFilter> queue = new LinkedBlockingDeque<>(processors);
            while (!queue.isEmpty()) {
                data = queue.poll().process(data);
            }

            return new ByteArrayInputStream(data);

        }

        public String process(String in) {
            if (processors.isEmpty()) {
                return in;
            }

            byte[] data = in.getBytes(StandardCharsets.UTF_8);
            Queue<ResourceFilter> queue = new LinkedBlockingDeque<>(processors);
            while (!queue.isEmpty()) {
                data = queue.poll().process(data);
            }

            return new String(data, Charset.defaultCharset());
        }
    }
}
